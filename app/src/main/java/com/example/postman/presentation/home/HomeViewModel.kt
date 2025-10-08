package com.example.postman.presentation.home

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postman.common.extensions.buildUrlWithParams
import com.example.postman.common.extensions.getNetworkErrorMessage
import com.example.postman.common.extensions.mapKeyValuePairsToQueryParameter
import com.example.postman.common.utils.MethodName
import com.example.postman.data.local.dao.CollectionDao
import com.example.postman.data.mapper.CollectionMapper.toHttpRequest
import com.example.postman.data.mapper.CollectionMapper.toHttpResponse
import com.example.postman.data.mapper.HistoryMapper
import com.example.postman.data.mapper.HistoryMapper.toHttpRequest
import com.example.postman.data.mapper.HistoryMapper.toHttpResponse
import com.example.postman.data.mapper.toDomain
import com.example.postman.domain.model.HttpRequest
import com.example.postman.domain.model.HttpResult
import com.example.postman.domain.repository.ApiService
import com.example.postman.domain.repository.HistoryRepository
import com.example.postman.presentation.base.Loadable
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readBytes
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ApiService,
    private val historyRepository: HistoryRepository,
    private val collectionDao: CollectionDao
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(
        HomeUiState(HttpRequest())
    )

    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun sendRequest() {
        val requestData = uiState.value.data
        if (requestData.requestUrl.isBlank()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(response = Loadable.Loading)
            try {
                val result = repository.sendRequest(
                    method = requestData.methodOption.name,
                    url = requestData.baseUrl,
                    headers = requestData.headers,
                    parameters = requestData.params,
                    body = requestData.body
                )
                val response = buildHttpResponse(result)

                _uiState.value = _uiState.value.copy(
                    response = Loadable.Success(response)
                )
                saveToHistory(
                    requestData,
                    response
                )
            } catch (error: Exception) {
                _uiState.value = _uiState.value.copy(
                    response = Loadable.NetworkError(
                        error.getNetworkErrorMessage()
                    )
                )
                saveToHistory(
                    requestData,
                    HttpResult(error.getNetworkErrorMessage())
                )
            }
        }
    }

    fun clearData() {
        _uiState.value = HomeUiState(HttpRequest(), null)
    }

    fun updateRequestUrl(newUrl: String) {
        _uiState.value =
            _uiState.value.copy(
                data = _uiState.value.data.copy(
                    requestUrl = newUrl,
                )
            )
    }

    fun updateMethodName(newMethodName: MethodName) {
        _uiState.value = _uiState.value.copy(_uiState.value.data.copy(methodOption = newMethodName))
    }

    fun updateBody(body: String) {
        _uiState.value = _uiState.value.copy(_uiState.value.data.copy(body = body))
    }

    fun addHeader(key: String, value: String) {
        val currentHeaders = _uiState.value.data.headers.orEmpty().toMutableList()

        if (key.isBlank() || value.isBlank())
            return
        val modifiedValue = if (key.equals("Authorization", ignoreCase = true)) {
            currentHeaders.removeIf { it.first.equals("Authorization", ignoreCase = true) }
            if (value.startsWith("Bearer ")) {
                value
            } else "Bearer $value"
        } else {
            value
        }

        currentHeaders.add(key to modifiedValue)

        _uiState.value =
            _uiState.value.copy(data = _uiState.value.data.copy(headers = currentHeaders))
    }

    fun removeHeader(key: String, value: String) {
        val currentHeaders = _uiState.value.data.headers
        val updatedHeaders = currentHeaders?.filterNot { it.first == key && it.second == value }
        _uiState.value =
            _uiState.value.copy(
                data = _uiState.value.data.copy(
                    headers = updatedHeaders,
                )
            )
    }

    fun addParameter(key: String, value: String) {
        if (key.isBlank()) return
        val currentParams = _uiState.value.data.params
        val updatedParams = currentParams.orEmpty() + (key to value)
        updateParamsUiState(updatedParams)
    }

    fun removeParameter(key: String, value: String) {
        val originalUrl = _uiState.value.data.requestUrl
        val uri = originalUrl.toUri()

        val newParams = uri.queryParameterNames.flatMap { paramKey ->
            uri.getQueryParameters(paramKey).mapNotNull { paramValue ->
                if (paramKey == key && paramValue == value) null else "$paramKey=$paramValue"
            }
        }

        val baseUrl = originalUrl.substringBefore("?")
        val newUrl = if (newParams.isEmpty()) baseUrl else "$baseUrl?${newParams.joinToString("&")}"

        updateRequestUrl(newUrl)
    }

    fun updateParamsUiState(params: List<Pair<String, String>>) {
        val url = buildUrlWithParams(
            _uiState.value.data.requestUrl,
            params.mapKeyValuePairsToQueryParameter()
        )
        _uiState.value =
            _uiState.value.copy(
                data = _uiState.value.data.copy(
                    requestUrl = url,
                )
            )
    }

    suspend fun buildHttpResponse(result: HttpResponse): HttpResult {
        val (responseBody, imageResponse) = buildResponseBody(result)
        return HttpResult(
            response = responseBody,
            statusCode = result.status.value,
            imageResponse = imageResponse
        )
    }

    suspend fun buildResponseBody(
        result: HttpResponse
    ): Pair<String, ImageBitmap?> {
        var imageResponse: ImageBitmap? = null

        val statusCode = result.status.value
        val contentType = result.contentType()?.toString() ?: ""

        val responseBody = if (statusCode in 200..299) {
            when {
                contentType == "image/svg+xml" -> {
                    result.bodyAsText()
                }

                contentType.startsWith("image/") -> {
                    val imageBytes = result.readBytes()
                    val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    imageResponse = bitmap.asImageBitmap()

                    "This is an image of type $contentType with ${imageBytes.size / 1024} KB."
                }

                else -> {
                    result.bodyAsText()
                }
            }
        } else {
            result.bodyAsText()
        }

        return Pair(responseBody, imageResponse)
    }

    fun saveToHistory(
        httpRequest: HttpRequest,
        response: HttpResult
    ) {
        viewModelScope.launch {
            historyRepository.insertHistoryRequest(
                HistoryMapper.httpRequestToHistory(httpRequest, response)
            )
        }
    }

    fun loadRequestFromHistory(historyId: Int) {
        viewModelScope.launch {
            val saved = historyRepository.getHistoryRequest(historyId)
            val response = if (saved.statusCode != null)
                Loadable.Success(
                    saved.toHttpResponse()
                )
            else
                Loadable.Error(
                    saved.toHttpResponse().response
                )

            _uiState.value = HomeUiState(
                saved.toHttpRequest(),
                response
            )
        }
    }

    fun loadRequestFromCollection(requestId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val saved = collectionDao.getCollectionRequest(requestId).toDomain()
            val response = if (saved.statusCode != null)
                Loadable.Success(
                    saved.toHttpResponse()
                )
            else
                Loadable.Error(
                    saved.toHttpResponse().response
                )

            _uiState.value = HomeUiState(
                saved.toHttpRequest(),
                response
            )
        }
    }
}
package com.example.postman.presentation.home

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postman.common.extensions.buildUrlWithParams
import com.example.postman.common.extensions.getNetworkErrorMessage
import com.example.postman.common.extensions.mapKeyValuePairsToQueryParameter
import com.example.postman.common.utils.MethodName
import com.example.postman.data.mapper.HistoryMapper
import com.example.postman.data.mapper.HistoryMapper.toHttpRequest
import com.example.postman.data.mapper.HistoryMapper.toHttpResponse
import com.example.postman.domain.model.HttpRequest
import com.example.postman.domain.model.HttpResponse
import com.example.postman.domain.repository.ApiRepository
import com.example.postman.domain.repository.HistoryRepository
import com.example.postman.domain.repository.QueryParamsRepository
import com.example.postman.domain.repository.RequestHeaderRepository
import com.example.postman.presentation.base.Loadable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject
import androidx.core.net.toUri

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ApiRepository,
    private val headerRepository: RequestHeaderRepository,
    private val parameterRepository: QueryParamsRepository,
    private val historyRepository: HistoryRepository
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
                val result = repository.request(
                    method = requestData.methodOption.name,
                    url = requestData.requestUrl,
                    body = requestData.body?.toRequestBody()
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
                    HttpResponse(error.getNetworkErrorMessage())
                )
            }
        }
    }

    fun clearData() {
        clearHeaders()
        clearParameters()
        _uiState.value = HomeUiState(HttpRequest(), null)
    }

    fun updateRequestUrl(newUrl: String) {
//        parameterRepository.updateParameter(newUrl.mapStringToKeyValuePairs())

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

    private fun getHeaders(): List<Pair<String, String>>? {
        return headerRepository.getHeaders().toList()
    }

    fun addHeader(key: String, value: String) {
        headerRepository.addHeader(key, value)
        _uiState.value =
            _uiState.value.copy(
                data = _uiState.value.data.copy(
                    headers = getHeaders()
                )
            )
    }

    fun removeHeader(key: String, value: String) {
        headerRepository.removeHeader(key, value)
        _uiState.value =
            _uiState.value.copy(data = _uiState.value.data.copy(headers = getHeaders()))
    }

    private fun clearHeaders() {
        headerRepository.clearHeaders()
    }

    fun addParameter(key: String, value: String) {
        // parameterRepository.addParameter(key, value)
        val newParams = _uiState.value.data.params?.toMutableList()?.apply {
            if (key.isBlank() || value.isBlank())
                return@apply
            add(Pair(key, value))
        } ?: emptyList()
        updateParamsUiState(newParams)
    }

    fun removeParameter(key: String, value: String) {
        val originalUrl = _uiState.value.data.requestUrl
        val uri = originalUrl.toUri()

        val newParams = uri.queryParameterNames
            .flatMap { paramKey ->
                uri.getQueryParameters(paramKey).mapNotNull { paramValue ->
                    if (paramKey == key && paramValue == value) null else "$paramKey=$paramValue"
                }
            }

        val baseUrl = originalUrl.substringBefore("?")
        val newUrl = if (newParams.isEmpty()) baseUrl
        else "$baseUrl?${newParams.joinToString("&")}"

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

    private fun getParameters(): List<Pair<String, String>> {
        return parameterRepository.getParameters().toList()
    }

    private fun clearParameters() {
        parameterRepository.clearParameters()
    }


    fun buildHttpResponse(result: Response<ResponseBody>): HttpResponse {
        val (responseBody, imageResponse) = buildResponseBody(result)
        return HttpResponse(
            response = responseBody,
            statusCode = result.code(),
            imageResponse = imageResponse
        )
    }

    private fun buildResponseBody(
        result: Response<ResponseBody>,
    ): Pair<String, ImageBitmap?> {
        var imageResponse: ImageBitmap? = null

        val responseBody = if (result.isSuccessful) {
            val contentType = result.body()?.contentType()?.toString() ?: ""
            when {
                contentType == "image/svg+xml" -> {
                    result.body()?.string() ?: "Empty svg"
                }

                contentType.startsWith("image/") -> {
                    val imageBytes = result.body()?.bytes()
                    val bitmap =
                        BitmapFactory.decodeByteArray(
                            imageBytes,
                            0,
                            imageBytes?.size ?: 0
                        )
                    imageResponse = bitmap.asImageBitmap()

                    "This is an image of type $contentType with ${(imageBytes?.size ?: 0) / 1024} KB."
                }

                else -> {
                    result.body()?.string() ?: "Empty"
                }
            }
        } else {
            result.errorBody()?.string() ?: "Something wrong!!!"
        }
        return Pair(responseBody, imageResponse)
    }

    fun saveToHistory(
        httpRequest: HttpRequest,
        response: HttpResponse
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
}
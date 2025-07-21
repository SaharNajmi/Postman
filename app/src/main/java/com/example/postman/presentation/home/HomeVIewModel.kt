package com.example.postman.presentation.home

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postman.common.extensions.getNetworkErrorMessage
import com.example.postman.data.mapper.HistoryMapper
import com.example.postman.data.mapper.HistoryMapper.toHttpRequest
import com.example.postman.data.mapper.HistoryMapper.toHttpResponse
import com.example.postman.domain.model.HttpRequest
import com.example.postman.domain.model.HttpResponse
import com.example.postman.domain.repository.ApiRepository
import com.example.postman.domain.repository.HistoryRepository
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

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ApiRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(
        HomeUiState(HttpRequest())
    )

    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun updateRequest(request: HttpRequest) {
        _uiState.value = _uiState.value.copy(request)
    }

    fun addHeader(key: String, value: String) {
        if (key.isNotBlank() && value.isNotBlank()) {
            val updatedHeaders = _uiState.value.data.headers?.toMutableMap() ?: mutableMapOf()
            updatedHeaders[key] = value
            _uiState.value =
                _uiState.value.copy(data = _uiState.value.data.copy(headers = updatedHeaders))
        }
    }

    fun removeHeader(key: String, value: String) {
        val updatedHeaders = _uiState.value.data.headers?.toMutableMap() ?: return
        updatedHeaders.remove(key, value)
        _uiState.value =
            _uiState.value.copy(data = _uiState.value.data.copy(headers = updatedHeaders))
    }

    fun sendRequest() {
        val requestData = uiState.value.data
        if (requestData.requestUrl.isBlank()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(response = Loadable.Loading)
            try {
                val result = repository.request(
                    method = requestData.methodOption.name,
                    url = requestData.requestUrl,
                    body = requestData.body?.toRequestBody(),
                    headers = requestData.headers
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
            }
        }
    }

    fun buildHttpResponse(result: Response<ResponseBody>): HttpResponse {
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

        return HttpResponse(
            response = responseBody,
            statusCode = result.code(),
            imageResponse = imageResponse
        )
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

            _uiState.value = HomeUiState(
                saved.toHttpRequest(),
                Loadable.Success(
                    saved.toHttpResponse()
                )
            )
        }
    }
}
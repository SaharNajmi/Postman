package com.example.postman.presentation.home

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        if (requestData.requestUrl.isNotBlank())
            viewModelScope.launch {
                _uiState.value = _uiState.value.copy(response = Loadable.Loading)
                try {
                    var imageResponse: ImageBitmap? = null
                    val result = repository.request(
                        method = requestData.methodOption.name,
                        url = requestData.requestUrl,
                        body = requestData.body?.toRequestBody(),
                        headers = requestData.headers
                    )

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

                    _uiState.value = _uiState.value.copy(
                        response = Loadable.Success(
                            HttpResponse(
                                response = responseBody,
                                statusCode = result.code(),
                                imageResponse = imageResponse
                            )
                        )
                    )
                    saveToHistory(
                        requestData,
                        HttpResponse(
                            response = responseBody,
                            statusCode = result.code(),
                            imageResponse = imageResponse
                        )
                    )
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        response = Loadable.NetworkError(
                            getNetworkErrorMessage(e)
                        )
                    )
                }
            }
    }

    fun getNetworkErrorMessage(e: Exception): String {
        return when (e) {
            is java.net.UnknownHostException -> "Unknown Host"
            is java.net.SocketTimeoutException -> "Connection timed out"
            is java.net.ConnectException -> "Couldn't connect to the server"
            is retrofit2.HttpException -> {
                val code = e.code()
                when (code) {
                    400 -> "Bad request."
                    401 -> "You are not authorized."
                    403 -> "Access denied."
                    404 -> "Not found."
                    405 -> "This operation isn’t allowed."
                    500 -> "Server error. Please try again later."
                    else -> "HTTP error: $code"
                }
            }

            else -> "Unexpected error: ${e.localizedMessage}"
        }
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
                    saved.toHttpResponse()))
        }
    }
}
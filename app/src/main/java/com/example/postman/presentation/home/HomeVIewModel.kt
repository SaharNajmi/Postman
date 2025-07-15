package com.example.postman.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postman.common.utils.MethodName
import com.example.postman.domain.model.History
import com.example.postman.domain.model.HttpRequest
import com.example.postman.domain.model.HttpResponse
import com.example.postman.domain.repository.ApiRepository
import com.example.postman.domain.repository.HistoryRepository
import com.example.postman.presentation.base.Loadable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        HomeUiState(
            HttpRequest( requestUrl = "", methodOption =  MethodName.GET)
        )
    )

    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun updateRequest(request: HttpRequest) {
        _uiState.value = _uiState.value.copy(request)
    }

    fun sendRequest(
        methodName: MethodName,
        url: String,
        body: String? = null
    ) {
        viewModelScope.launch {
            _uiState.value = HomeUiState(HttpRequest(requestUrl = url, methodOption =  methodName), Loadable.Loading)
            var result: Response<ResponseBody>? = null
            var statusCode = -1
            var body = ""
            try {
                result = withContext(Dispatchers.IO) {
                    repository.request(methodName.name, url, body.toRequestBody())
                }
                statusCode = result.code()
                body = result.body()?.string() ?: "Empty"
                _uiState.value = HomeUiState(
                    HttpRequest(
                        requestUrl = url,
                        methodOption = methodName,
                    ), Loadable.Success(
                        HttpResponse(
                            response = body,
                            statusCode = statusCode
                        )
                    )
                )
                saveToHistory(
                    HttpRequest(
                        requestUrl = url,
                        methodOption = methodName,
                    ), HttpResponse(
                        response = body,
                        statusCode = statusCode
                    )
                )
            } catch (e: Exception) {
                _uiState.value = HomeUiState(
                    HttpRequest(
                        requestUrl = url,
                        methodOption = methodName,
                    ), Loadable.NetworkError(getNetworkErrorMessage(e))
                )
            }
        }
    }


    fun getNetworkErrorMessage(e: Exception): String {
        return when (e) {
            is java.net.UnknownHostException -> "No internet connection"
            is java.net.SocketTimeoutException -> "Connection timed out"
            is java.net.ConnectException -> "Couldn't connect to the server"
            is retrofit2.HttpException -> {
                val code = e.code()
                when (code) {
                    401 -> "Unauthorized"
                    404 -> "Not found"
                    500 -> "Server error"
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
            withContext(Dispatchers.IO) {
                historyRepository.insertHistoryRequest(
                    History(
                        requestUrl = httpRequest.requestUrl,
                        methodOption = httpRequest.methodOption,
                        createdAt = httpRequest.createdAt,
                        response = response.response,
                        statusCode = response.statusCode
                    )
                )
            }
        }
    }

    fun loadRequestFromHistory(historyId: Int) {
        viewModelScope.launch {
            val saved = withContext(Dispatchers.IO) {
                historyRepository.getHistoryRequest(historyId)
            }
            _uiState.value = HomeUiState(
                HttpRequest(
                    requestUrl = saved.requestUrl,
                    methodOption = saved.methodOption,
                ), Loadable.Success(
                    HttpResponse(
                        response = saved.response,
                        statusCode = saved.statusCode
                    )
                )
            )
        }
    }
}
package com.example.postman.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postman.data.remote.UiState
import com.example.postman.data.repository.ApiRepository
import com.example.postman.domain.model.HistoryRequestModel
import com.example.postman.domain.repository.HistoryRequestRepository
import com.example.postman.presentation.MethodName
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
    private val historyRequestRepository: HistoryRequestRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val uiState: StateFlow<UiState<String>> = _uiState.asStateFlow()

    private val _historyRequest = MutableStateFlow<HistoryRequestModel?>(null)
    val historyRequest: StateFlow<HistoryRequestModel?> = _historyRequest.asStateFlow()

    fun sendRequest(
        methodName: MethodName,
        url: String,
        body: String? = null
    ) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            var result: Response<ResponseBody>? = null
            var statusCode = -1
            var body = ""
            try {
                result = withContext(Dispatchers.IO) {
                    repository.request(methodName.name, url, body?.toRequestBody())
                }
                statusCode = result.code()
                body = result.body()?.string() ?: "Empty"
                _uiState.value = when (result.code()) {
                    200 -> UiState.Success(body)
                    404 -> UiState.Error("${result.code()}: ${result.message()} Not Found")
                    else -> UiState.Error("error: ${result.code()}")
                }
            } catch (e: Exception) {
                statusCode = 404
                _uiState.value = UiState.Error("Network error: ${e.message}")
            } finally {
                saveToHistory(
                    HistoryRequestModel(
                        requestUrl = url,
                        methodOption = methodName,
                        response = body,
                        statusCode = statusCode
                    )
                )
            }
        }
    }

    fun saveToHistory(
        historyRequestModel: HistoryRequestModel
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                historyRequestRepository.insertHistoryRequest(
                    historyRequestModel
                )
            }
        }
    }

    fun loadRequestFromHistory(historyId: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val saved = withContext(Dispatchers.IO) {
                historyRequestRepository.getHistoryRequest(historyId)
            }
            _historyRequest.value = saved
            _uiState.value = when (saved.statusCode) {
                200 -> UiState.Success(saved.response)
                404 -> UiState.Error("${saved.statusCode} Not Found")
                else -> UiState.Error("error: ${saved.statusCode}")
            }
        }
    }
}
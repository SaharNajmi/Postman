package com.example.postman.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postman.presentation.base.BaseUiState
import com.example.postman.domain.repository.ApiRepository
import com.example.postman.domain.model.History
import com.example.postman.domain.repository.HistoryRepository
import com.example.postman.common.utils.MethodName
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

    private val _uiState = MutableStateFlow<BaseUiState<String>>(BaseUiState.Idle)
    val uiState: StateFlow<BaseUiState<String>> = _uiState.asStateFlow()

    private val _historyRequest = MutableStateFlow<History?>(null)
    val historyRequest: StateFlow<History?> = _historyRequest.asStateFlow()

    fun sendRequest(
        methodName: MethodName,
        url: String,
        body: String? = null
    ) {
        viewModelScope.launch {
            _uiState.value = BaseUiState.Loading
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
                    200 -> BaseUiState.Success(body)
                    404 -> BaseUiState.Error("${result.code()}: ${result.message()} Not Found")
                    else -> BaseUiState.Error("error: ${result.code()}")
                }
            } catch (e: Exception) {
                statusCode = 404
                _uiState.value = BaseUiState.Error("Network error: ${e.message}")
            } finally {
                saveToHistory(
                    History(
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
        history: History
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                historyRepository.insertHistoryRequest(
                    history
                )
            }
        }
    }

    fun loadRequestFromHistory(historyId: Int) {
        viewModelScope.launch {
            _uiState.value = BaseUiState.Loading
            val saved = withContext(Dispatchers.IO) {
                historyRepository.getHistoryRequest(historyId)
            }
            _historyRequest.value = saved
            _uiState.value = when (saved.statusCode) {
                200 -> BaseUiState.Success(saved.response)
                404 -> BaseUiState.Error("${saved.statusCode} Not Found")
                else -> BaseUiState.Error("error: ${saved.statusCode}")
            }
        }
    }
}
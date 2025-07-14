package com.example.postman.presentation.history

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postman.domain.model.HistoryRequestModel
import com.example.postman.domain.repository.HistoryRequestRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryViewModel(private val historyRequestRepository: HistoryRequestRepository) :
    ViewModel() {

    private val _historyRequestsModel = MutableStateFlow<List<HistoryRequestModel>>(emptyList())
    val historyRequestsModel: StateFlow<List<HistoryRequestModel>> = _historyRequestsModel

    private val _historyItem = MutableStateFlow<HistoryRequestModel?>(null)
    val historyItem: StateFlow<HistoryRequestModel?> = _historyItem

    fun getAllHistories() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                historyRequestRepository.getAllHistories()
            }
            _historyRequestsModel.value = result // main thread
        }
    }

    fun insertHistoryRequest(history: HistoryRequestModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                historyRequestRepository.insertHistoryRequest(history)
            }
        }
    }

    fun updateHistoryRequest(historyItem: HistoryRequestModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                historyRequestRepository.updateHistoryRequest(historyItem)
            }
        }
    }

    fun deleteHistoryRequest(history: HistoryRequestModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                historyRequestRepository.deleteHistoryRequest(history)
            }
            getAllHistories()
        }
    }

    fun getHistoryRequest(historyId: Int) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                historyRequestRepository.getHistoryRequest(historyId)
            }
            _historyItem.value = result
        }
    }
}
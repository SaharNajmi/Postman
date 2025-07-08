package com.example.postman.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postman.domain.model.HistoryRequest
import com.example.postman.domain.repository.HistoryRequestRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(private val historyRequestRepository: HistoryRequestRepository) :
    ViewModel() {

    private val _historyRequests = MutableStateFlow<List<HistoryRequest>>(emptyList())
    val historyRequests: StateFlow<List<HistoryRequest>> = _historyRequests

    private val _historyItem = MutableStateFlow<HistoryRequest?>(null)
    val historyItem: StateFlow<HistoryRequest?> = _historyItem

    fun getAllHistories() {
        viewModelScope.launch(Dispatchers.IO) {
            _historyRequests.value = historyRequestRepository.getAllHistories()
        }
    }

    fun insertHistoryRequest(history: HistoryRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            historyRequestRepository.insertHistoryRequest(history)
        }
    }

    fun updateHistoryRequest(historyItem: HistoryRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            historyRequestRepository.updateHistoryRequest(historyItem)
        }
    }

    fun deleteHistoryRequest(history: HistoryRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            historyRequestRepository.deleteHistoryRequest(history)
        }
    }

    fun getHistoryRequest(historyId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _historyItem.value = historyRequestRepository.getHistoryRequest(historyId)
        }
    }
}
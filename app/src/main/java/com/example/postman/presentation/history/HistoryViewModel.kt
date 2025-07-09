package com.example.postman.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postman.domain.model.HistoryRequestModel
import com.example.postman.domain.repository.HistoryRequestRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(private val historyRequestRepository: HistoryRequestRepository) :
    ViewModel() {

    private val _historyRequestsModel = MutableStateFlow<List<HistoryRequestModel>>(emptyList())
    val historyRequestsModel: StateFlow<List<HistoryRequestModel>> = _historyRequestsModel

    private val _historyItem = MutableStateFlow<HistoryRequestModel?>(null)
    val historyItem: StateFlow<HistoryRequestModel?> = _historyItem

    fun getAllHistories() {
        viewModelScope.launch(Dispatchers.IO) {
            _historyRequestsModel.value = historyRequestRepository.getAllHistories()
        }
    }

    fun insertHistoryRequest(history: HistoryRequestModel) {
        viewModelScope.launch(Dispatchers.IO) {
            historyRequestRepository.insertHistoryRequest(history)
        }
    }

    fun updateHistoryRequest(historyItem: HistoryRequestModel) {
        viewModelScope.launch(Dispatchers.IO) {
            historyRequestRepository.updateHistoryRequest(historyItem)
        }
    }

    fun deleteHistoryRequest(history: HistoryRequestModel) {
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
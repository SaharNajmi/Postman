package com.example.postman.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postman.domain.model.History
import com.example.postman.domain.repository.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private val _history = MutableStateFlow<List<History>>(emptyList())
    val historyRequestsModel: StateFlow<List<History>> = _history

    private val _historyItem = MutableStateFlow<History?>(null)
    val historyItem: StateFlow<History?> = _historyItem

    fun getAllHistories() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                historyRepository.getAllHistories()
            }
            _history.value = result // main thread
        }
    }

    fun insertHistoryRequest(history: History) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                historyRepository.insertHistoryRequest(history)
            }
        }
    }

    fun updateHistoryRequest(historyItem: History) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                historyRepository.updateHistoryRequest(historyItem)
            }
        }
    }

    fun deleteHistoryRequest(history: History) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                historyRepository.deleteHistoryRequest(history)
            }
            getAllHistories()
        }
    }

    fun getHistoryRequest(historyId: Int) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                historyRepository.getHistoryRequest(historyId)
            }
            _historyItem.value = result
        }
    }
}
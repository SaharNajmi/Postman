package com.example.postman.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postman.common.utils.formatDate
import com.example.postman.domain.model.History
import com.example.postman.domain.repository.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private val _httpRequestRequestsModel =
        MutableStateFlow<Map<String, List<History>>>(mapOf())
    val httpRequestRequestsModel: StateFlow<Map<String, List<History>>> = _httpRequestRequestsModel

    fun getAllHistories() {
        viewModelScope.launch {
            val result =
                historyRepository.getAllHistories()
            _httpRequestRequestsModel.value = result.groupBy { formatDate(it.createdAt) }
        }
    }

    fun deleteHistoryRequest(historyId: Int) {
        viewModelScope.launch {
            historyRepository.deleteHistoryRequest(historyId)
            getAllHistories()
        }
    }

    fun deleteHistoriesRequest(historyIds: List<Int>) {
        viewModelScope.launch {
            historyRepository.deleteHistoriesRequest(historyIds)
            getAllHistories()
        }
    }
}
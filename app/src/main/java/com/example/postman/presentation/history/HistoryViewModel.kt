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

    private val _httpRequestRequestsModel = MutableStateFlow<List<History>>(emptyList())
    val httpRequestRequestsModel: StateFlow<List<History>> = _httpRequestRequestsModel

    fun getAllHistories() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                historyRepository.getAllHistories()
            }
            _httpRequestRequestsModel.value = result // main thread
        }
    }

    fun deleteHistoryRequest(historyId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                historyRepository.deleteHistoryRequest(historyId)
            }
            getAllHistories()
        }
    }
}
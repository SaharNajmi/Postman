package com.example.postman.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postman.common.utils.formatDate
import com.example.postman.data.local.dao.CollectionDao
import com.example.postman.data.mapper.toRequestEntity
import com.example.postman.domain.model.History
import com.example.postman.domain.repository.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyRepository: HistoryRepository,
    val collectionDao: CollectionDao
) : ViewModel() {

    private val _httpRequestRequestsModel =
        MutableStateFlow<Map<String, List<History>>>(mapOf())
    val httpRequestRequestsModel: StateFlow<Map<String, List<History>>> = _httpRequestRequestsModel

    private val _expandedStates = MutableStateFlow<Map<String, Boolean>>(mapOf())
    val expandedStates: StateFlow<Map<String, Boolean>> = _expandedStates

    private val _collectionNames =
        MutableStateFlow<Map<String, String>>(mapOf())
    val collectionNames: StateFlow<Map<String, String>> = _collectionNames

    fun getAllHistories() {
        viewModelScope.launch {
            val result =
                historyRepository.getAllHistories()
            _httpRequestRequestsModel.value = result.groupBy { formatDate(it.createdAt) }
        }
    }

    fun toggleExpanded(date: String) {
        _expandedStates.value =
            _expandedStates.value.toMutableMap().apply { this[date] = this[date]?.not() ?: false }
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

    fun getCollections() {
        val thread = Thread {
            val collections = collectionDao.getAllCollections()
            _collectionNames.value = collections.associate {
                it.collectionId to it.collectionName
            }
        }
        thread.start()
    }

    fun addRequestToCollection(request: History, collectionId: String) {
        val thread = Thread {
            val entity = request.toRequestEntity(collectionId)
            collectionDao.insertRequestToCollection(entity)
        }
        thread.start()
    }

    fun addRequestsToCollection(
        requests: List<History>,
        collectionId: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            requests.forEach { request ->
                val entity = request.toRequestEntity(collectionId)
                collectionDao.insertRequestToCollection(entity)
            }
        }
    }
}
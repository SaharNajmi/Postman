package com.example.postman.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postman.common.utils.formatDate
import com.example.postman.data.local.dao.CollectionDao
import com.example.postman.data.mapper.toRequestEntity
import com.example.postman.domain.model.CollectionEntry
import com.example.postman.domain.model.History
import com.example.postman.domain.model.HistoryEntry
import com.example.postman.domain.repository.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.component1
import kotlin.collections.component2

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyRepository: HistoryRepository,
    val collectionDao: CollectionDao,
) : ViewModel() {

    private val _historyEntry =
        MutableStateFlow<List<HistoryEntry>>(listOf())
    val historyEntry: StateFlow<List<HistoryEntry>> = _historyEntry

    private val _expandedStates = MutableStateFlow<Map<String, Boolean>>(mapOf())
    val expandedStates: StateFlow<Map<String, Boolean>> = _expandedStates

    private val _collectionNames =
        MutableStateFlow<Set<CollectionEntry>>(setOf())
    val collectionNames: StateFlow<Set<CollectionEntry>> = _collectionNames

    fun getHistories() {
        viewModelScope.launch {
            val result =
                historyRepository.getAllHistories()
            val grouped: Map<String, List<History>> = result.groupBy { formatDate(it.createdAt) }
            _historyEntry.value = grouped.map { (date, histories) ->
                HistoryEntry(dateCreated = date, histories = histories)
            }
        }
    }

    fun toggleExpanded(date: String) {
        _expandedStates.value =
            _expandedStates.value.toMutableMap().apply { this[date] = this[date]?.not() ?: false }
    }

    fun deleteHistoryRequest(historyId: Int) {
        viewModelScope.launch {
            historyRepository.deleteHistoryRequest(historyId)
            getHistories()
        }
    }

    fun deleteHistoriesRequest(historyIds: List<Int>) {
        viewModelScope.launch {
            historyRepository.deleteHistoriesRequest(historyIds)
            getHistories()
        }
    }

    fun getCollections() {
        val thread = Thread {
            val collections = collectionDao.getAllCollections()
            _collectionNames.value = collections.map {
                CollectionEntry(it.collectionId, it.collectionName)
            }.toSet()
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
        collectionId: String,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            requests.forEach { request ->
                val entity = request.toRequestEntity(collectionId)
                collectionDao.insertRequestToCollection(entity)
            }
        }
    }
}
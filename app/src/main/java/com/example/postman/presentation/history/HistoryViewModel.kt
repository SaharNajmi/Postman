package com.example.postman.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postman.common.utils.formatDate
import com.example.postman.data.mapper.toDomain
import com.example.postman.data.mapper.toRequestEntity
import com.example.postman.domain.model.CollectionEntry
import com.example.postman.domain.model.ExpandableHistoryItem
import com.example.postman.domain.model.History
import com.example.postman.domain.model.HistoryEntry
import com.example.postman.domain.repository.CollectionRepository
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
    val collectionRepository: CollectionRepository,
) : ViewModel() {

    private val _historyEntry =
        MutableStateFlow<List<HistoryEntry>>(listOf())
    val historyEntry: StateFlow<List<HistoryEntry>> = _historyEntry

    private val _expandedStates = MutableStateFlow<List<ExpandableHistoryItem>>(listOf())
    val expandedStates: StateFlow<List<ExpandableHistoryItem>> = _expandedStates

    private val _collectionNames =
        MutableStateFlow<Set<CollectionEntry>>(setOf())
    val collectionNames: StateFlow<Set<CollectionEntry>> = _collectionNames

    fun getHistories() {
        viewModelScope.launch {
            val result =
                historyRepository.getAllHistories()
            val grouped: Map<String, List<History>> = result.groupBy { formatDate(it.createdAt) }
            _historyEntry.value = grouped.map { (date, histories) ->
                _expandedStates.value = _expandedStates.value + ExpandableHistoryItem(date, false)
                HistoryEntry(dateCreated = date, histories = histories)
            }
        }
    }

    fun toggleExpanded(dateCreated: String) {
        _expandedStates.value =
            _expandedStates.value.map {
                if (it.dateCreated == dateCreated) it.copy(isExpanded = !it.isExpanded)
                else it
            }
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
        viewModelScope.launch {
            val collections = collectionRepository.getAllCollections()
            _collectionNames.value = collections.map {
                CollectionEntry(it.collectionId, it.collectionName)
            }.toSet()
        }
    }

    fun addRequestToCollection(request: History, collectionId: String) {
        viewModelScope.launch {
            collectionRepository.insertRequestToCollection(
                collectionId,
                request.toRequestEntity(collectionId).toDomain()
            )
        }
    }

    fun addRequestsToCollection(
        requests: List<History>,
        collectionId: String,
    ) {
        viewModelScope.launch {
            requests.map { it.toRequestEntity(collectionId).toDomain() }
                .forEach { collectionRepository.insertRequestToCollection(collectionId, it) }
        }
    }
}
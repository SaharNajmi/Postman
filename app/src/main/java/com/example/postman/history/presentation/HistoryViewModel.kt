package com.example.postman.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postman.collection.domain.repository.CollectionRepository
import com.example.postman.collection.presentation.model.CollectionEntry
import com.example.postman.core.data.mapper.toDomain
import com.example.postman.history.presentation.model.ExpandableHistoryItem
import com.example.postman.history.data.mapper.toEntity
import com.example.postman.history.domain.formatDate
import com.example.postman.history.domain.model.History
import com.example.postman.history.domain.repository.HistoryRepository
import com.example.postman.history.presentation.model.HistoryEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyRepository: HistoryRepository,
    val collectionRepository: CollectionRepository,
    private val dispatcher: CoroutineDispatcher,
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
        viewModelScope.launch(dispatcher) {
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
        viewModelScope.launch(dispatcher) {
            historyRepository.deleteHistoryRequest(historyId)
            getHistories()
        }
    }

    fun deleteHistoriesRequest(historyIds: List<Int>) {
        viewModelScope.launch(dispatcher) {
            historyRepository.deleteHistoriesRequest(historyIds)
            getHistories()
        }
    }

    fun getCollections() {
        viewModelScope.launch(dispatcher) {
            val collections = collectionRepository.getAllCollections()
            _collectionNames.value = collections.map {
                CollectionEntry(it.collectionId, it.collectionName)
            }.toSet()
        }
    }

    fun addRequestToCollection(request: History, collectionId: String) {
        viewModelScope.launch(dispatcher) {
            collectionRepository.insertRequestToCollection(
                collectionId,
                request.toEntity(collectionId).toDomain()
            )
        }
    }

    fun addRequestsToCollection(
        requests: List<History>,
        collectionId: String,
    ) {
        viewModelScope.launch(dispatcher) {
            requests.map { it.toEntity(collectionId).toDomain() }
                .forEach { collectionRepository.insertRequestToCollection(collectionId, it) }
        }
    }
}
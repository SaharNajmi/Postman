package com.example.postman.presentation.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postman.data.local.dao.CollectionDao
import com.example.postman.data.mapper.toDomain
import com.example.postman.data.mapper.toEntity
import com.example.postman.domain.model.Collection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val collectionDao: CollectionDao
) : ViewModel() {

    private val _collections =
        MutableStateFlow<List<Collection>>(emptyList())
    val collections: StateFlow<List<Collection>> = _collections


    private val _expandedStates = MutableStateFlow<Map<String, Boolean>>(mapOf())
    val expandedStates: StateFlow<Map<String, Boolean>> = _expandedStates

    fun getAllCollections() {
        val thread = Thread {
            _collections.value = collectionDao.getAllCollections().map { it.toDomain() }
//                .groupBy { it.collectionName }
//                .map { (name, items) ->
//                    Collection(
//                        requests = items.map { it.toDomain() },
//                        collectionName = name
//                    )
//                }
        }
        thread.start()
    }

    fun toggleExpanded(date: String) {
        _expandedStates.value =
            _expandedStates.value.toMutableMap().apply { this[date] = this[date]?.not() ?: false }
    }

    fun deleteRequestItem(requestId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            collectionDao.deleteRequestFromCollection(requestId)
            getAllCollections()
        }
    }

    fun deleteRequests(requestIds: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            collectionDao.deleteRequestsFromCollection(requestIds)
            getAllCollections()
        }
    }

    fun createNewCollection() {
        viewModelScope.launch(Dispatchers.IO) {
            collectionDao.insertRequestToCollections(Collection().toEntity())
        }
    }

    fun createAnEmptyRequest(collectionId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val newCollection = Collection(id = collectionId)
            collectionDao.insertRequestToCollections(newCollection.toEntity())
            getAllCollections()
        }
    }
}
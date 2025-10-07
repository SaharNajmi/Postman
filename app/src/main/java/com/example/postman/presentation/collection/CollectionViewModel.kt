package com.example.postman.presentation.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postman.data.local.dao.CollectionDao
import com.example.postman.data.local.entity.RequestEntity
import com.example.postman.data.mapper.toDomain
import com.example.postman.data.mapper.toEntity
import com.example.postman.domain.model.Collection
import com.example.postman.domain.model.Request
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

    fun getCollectionsWithRequests() {
        val thread = Thread {
            val collections = collectionDao.getAllCollections()
            _collections.value = collections.map {
                val requests = collectionDao.getRequestsForCollection(it.collectionId)
                it.toDomain(requests)
            }
        }
        thread.start()
    }


    fun toggleExpanded(collectionId: String) {
        _expandedStates.value =
            _expandedStates.value.toMutableMap()
                .apply { this[collectionId] = this[collectionId]?.not() ?: true }
    }

    fun deleteRequestItem(requestId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            collectionDao.deleteRequestFromCollection(requestId)
            getCollectionsWithRequests()
        }
    }

    fun deleteRequests(requestIds: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            collectionDao.deleteRequestsFromCollection(requestIds)
            getCollectionsWithRequests()
        }
    }

    fun deleteCollection(collectionId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            collectionDao.deleteCollection(collectionId)
            getCollectionsWithRequests()
        }
    }

    fun createNewCollection() {
        viewModelScope.launch(Dispatchers.IO) {
            collectionDao.insertCollection(Collection().toEntity())
            getCollectionsWithRequests()
        }
    }

    fun createAnEmptyRequest(collectionId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val newCollection = Request().toEntity(collectionId)
            collectionDao.insertRequestToCollection(newCollection)
            getCollectionsWithRequests()
        }
    }

    fun updateCollection(collection: Collection) {
        viewModelScope.launch(Dispatchers.IO) {
            collectionDao.updateCollection(collection.toEntity())
            getCollectionsWithRequests()
        }
    }
}
package com.example.postman.presentation.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postman.domain.model.Collection
import com.example.postman.domain.model.Request
import com.example.postman.domain.repository.CollectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val collectionRepository: CollectionRepository,
) : ViewModel() {

    private val _collections =
        MutableStateFlow<List<Collection>>(emptyList())
    val collections: StateFlow<List<Collection>> = _collections

    fun getCollections() {
        viewModelScope.launch(Dispatchers.IO) {
            _collections.value = collectionRepository.getAllCollections()
        }
    }

    fun toggleExpanded(collectionId: String) {
        _collections.value = _collections.value.map {
            if (it.collectionId == collectionId) {
                it.copy(isExpanded = !it.isExpanded)
            } else {
                it
            }
        }
    }

    fun deleteRequestItem(requestId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            collectionRepository.deleteRequestFromCollection(requestId)
            getCollections()
        }
    }

    fun deleteCollection(collectionId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            collectionRepository.deleteCollection(collectionId)
            getCollections()
        }
    }

    fun createNewCollection() {
        viewModelScope.launch(Dispatchers.IO) {
            collectionRepository.insertCollection(Collection())
            getCollections()
        }
    }

    fun createAnEmptyRequest(collectionId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            collectionRepository.insertRequestToCollection(collectionId, Request())
            getCollections()
        }
    }

    fun updateCollection(collection: Collection) {
        viewModelScope.launch(Dispatchers.IO) {
            collectionRepository.updateCollection(collection)
            getCollections()
        }
    }

    fun changeRequestName(requestId: Int, requestName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            collectionRepository.changeRequestName(requestId, requestName)
            getCollections()
        }
    }
}
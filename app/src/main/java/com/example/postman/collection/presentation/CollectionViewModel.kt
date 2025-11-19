package com.example.postman.collection.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postman.collection.domain.model.Collection
import com.example.postman.collection.domain.model.Request
import com.example.postman.collection.domain.repository.CollectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val collectionRepository: CollectionRepository,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _collections =
        MutableStateFlow<List<Collection>>(emptyList())
    val collections: StateFlow<List<Collection>> = _collections

    fun getCollections() {
        viewModelScope.launch(dispatcher) {
            _collections.value = collectionRepository.getAllCollections().map { item ->
                val expanded =
                    _collections.value.firstOrNull() { it.collectionId == item.collectionId }?.isExpanded
                        ?: false
                item.copy(isExpanded = expanded)
            }
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
        viewModelScope.launch(dispatcher) {
            collectionRepository.deleteRequestFromCollection(requestId)
            getCollections()
        }
    }

    fun deleteCollection(collectionId: String) {
        viewModelScope.launch(dispatcher) {
            collectionRepository.deleteCollection(collectionId)
            getCollections()
        }
    }

    fun createNewCollection() {
        viewModelScope.launch(dispatcher) {
            collectionRepository.insertCollection(Collection())
            getCollections()
        }
    }

    fun createAnEmptyRequest(collectionId: String) {
        viewModelScope.launch(dispatcher) {
            collectionRepository.insertRequestToCollection(collectionId, Request())
            getCollections()
        }
    }

    fun updateCollection(collection: Collection) {
        viewModelScope.launch(dispatcher) {
            collectionRepository.updateCollection(collection)
            getCollections()
        }
    }

    fun changeRequestName(requestId: Int, requestName: String) {
        viewModelScope.launch(dispatcher) {
            collectionRepository.changeRequestName(requestId, requestName)
            getCollections()
        }
    }
}
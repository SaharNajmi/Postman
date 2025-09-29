package com.example.postman.presentation.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postman.common.utils.formatDate
import com.example.postman.data.local.dao.CollectionDao
import com.example.postman.data.mapper.toDomain
import com.example.postman.domain.model.Collection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.concurrent.thread

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val collectionDao: CollectionDao
) : ViewModel() {

    private val _collections =
        MutableStateFlow<Map<String, List<Collection>>>(mapOf())
    val collections: StateFlow<Map<String, List<Collection>>> = _collections

    private val _expandedStates = MutableStateFlow<Map<String, Boolean>>(mapOf())
    val expandedStates: StateFlow<Map<String, Boolean>> = _expandedStates

    fun getAllCollections() {
        val thread = Thread {
            val result =
                collectionDao.getAllCollections().map { it.toDomain() }
            _collections.value = result.groupBy { formatDate(it.createdAt) }
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
}
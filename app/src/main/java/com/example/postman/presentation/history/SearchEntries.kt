package com.example.postman.presentation.history

import com.example.postman.domain.models.Collection
import com.example.postman.domain.models.HistoryEntry

fun searchHistories(
    entries: List<HistoryEntry>,
    searchQuery: String,
): List<HistoryEntry> {
    if (searchQuery.isBlank()) return entries

    return entries.mapNotNull { entry ->
        val filteredHistories = entry.histories.filter {
            it.requestUrl.contains(searchQuery, ignoreCase = true)
        }
        if (filteredHistories.isNotEmpty()) {
            entry.copy(histories = filteredHistories)
        } else {
            null
        }
    }
}

fun searchCollections(
    items: List<Collection>,
    searchQuery: String,
): List<Collection> {
    if (searchQuery.isBlank()) return items
    val result = mutableListOf<Collection>()
    items.forEach { collection ->
        val isQueryInCollectionName =
            collection.collectionName.contains(searchQuery, ignoreCase = true)
        if (isQueryInCollectionName) {
            result.add(collection)
        } else {
            val requests = collection.requests?.filter {
                it.requestName.contains(
                    searchQuery,
                    ignoreCase = true
                )
            }
            if (requests?.isNotEmpty() == true) {
                result.add(collection.copy(requests = requests))
            }
        }

    }
    return result
}
package com.example.postman.presentation.base

import com.example.postman.domain.model.Collection
import com.example.postman.domain.model.HistoryEntry

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
    return items.filter { collection ->
        collection.collectionName.contains(searchQuery, ignoreCase = true)
    }
}
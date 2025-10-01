package com.example.postman.presentation.base

import com.example.postman.domain.model.Collection

fun <T> searchEntries(
    entries: Map<String, List<T>>,
    searchQuery: String,
    match: (T, String) -> Boolean
): Map<String, List<T>> {

    return if (searchQuery.isBlank()) {
        entries
    } else {
        entries.mapValues { (key, values) ->
            values.filter {
//             it.contains(searchQuery, ignoreCase = true)
                match(it, searchQuery)
            }
        }.filterValues { it.isNotEmpty() }
    }
}

fun searchCollections(
    items: List<Collection>,
    searchQuery: String,
): List<Collection> {
    if (searchQuery.isBlank()) return items

    val result = mutableListOf<Collection>()
    items.groupBy { it.id to it.collectionName }.map { (key, requests) ->
        val (id, name) = key
        for (collection in requests) {
            if (collection.collectionName.contains(searchQuery, ignoreCase = true)) {
                result.add(collection)
            } else {
                val filteredRequests = collection.requestUrl.contains(
                    searchQuery,
                    ignoreCase = true
                )
                if (filteredRequests) {
                    result.add(collection)
                }
            }
        }
    }

    return result
}

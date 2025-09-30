package com.example.postman.presentation.base

import com.example.postman.domain.model.CollectionGroup

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
    items: List<CollectionGroup>,
    searchQuery: String,
): List<CollectionGroup> {
    if (searchQuery.isBlank()) return items

    val result = mutableListOf<CollectionGroup>()
    for (group in items) {
        if (group.collectionName.contains(searchQuery, ignoreCase = true)) {
            result.add(group)
        } else {
            val filteredRequests = group.requests.filter { request ->
                request.requestUrl.contains(
                    searchQuery,
                    ignoreCase = true
                )
            }
            if (filteredRequests.isNotEmpty()) {
                result.add(CollectionGroup(group.collectionName, filteredRequests))
            }
        }
    }
    return result
}

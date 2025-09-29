package com.example.postman.presentation.base

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
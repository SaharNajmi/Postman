package com.example.postman.common.extensions

fun List<Pair<String, String>>.getHeaderValue(key: String): String {
    return firstOrNull { it.first.equals(key, ignoreCase = true) }?.second ?: ""
}
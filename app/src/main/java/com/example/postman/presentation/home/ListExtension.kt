package com.example.postman.presentation.home

import com.example.postman.core.KeyValueList

fun KeyValueList.getHeaderValue(key: String): String {
    return firstOrNull { it.first.equals(key, ignoreCase = true) }?.second ?: ""
}
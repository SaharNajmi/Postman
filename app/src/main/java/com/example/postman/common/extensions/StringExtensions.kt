package com.example.postman.common.extensions

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser

fun String.formatJson(): String {
    return try {
        val jsonElement = JsonParser.parseString(this)
        GsonBuilder().setPrettyPrinting().create().toJson(jsonElement)
    } catch (e: Exception) {
        this
    }
}
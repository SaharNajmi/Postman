package com.example.postman.common.extensions

import com.example.postman.common.utils.HttpMethod
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

fun String.mapStringToKeyValuePairs(): List<Pair<String, String>> {
    if (this.isEmpty())
        return emptyList()

    val query = this.substringAfter("?", "")

    if (query.isEmpty())
        return emptyList()

    return query
        .split("&")
        .filter { it.isNotBlank() }
        .map {
            val parts = it.split("=")
            val key = parts.getOrNull(0) ?: ""
            val value = parts.getOrNull(1) ?: ""
            key to value
        }
}

fun List<Pair<String, String>>.mapKeyValuePairsToQueryParameter(): String {
    return this.joinToString("&") { (prefix, postfix) ->
        "$prefix=$postfix"
    }
}

fun buildUrlWithParams(requestUrl: String, queryParams: String): String {
    val baseUrl = requestUrl.substringBefore("?")
    return if (queryParams.isEmpty())
        requestUrl
    else
        "$baseUrl?$queryParams"
}

fun String.parseHttpMethodFromString(): HttpMethod{
    val methodPart: String = this.substringBefore(" ")
    return HttpMethod.valueOf(methodPart)
}

package com.example.postman.common.extensions

import androidx.core.net.toUri
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

fun String.buildUrlWithParams(queryParams: String): String {
    val baseUrl = this.substringBefore("?")
    return if (queryParams.isEmpty())
        this
    else
        "$baseUrl?$queryParams"
}

fun String.parseHttpMethodFromString(): HttpMethod {
    val methodPart: String = this.substringBefore(" ")
    return HttpMethod.valueOf(methodPart)
}

//fun String.removeParameterFromUrl( key: String, value: String): String {
//    val uri = this.toUri()
//
//    val newParams = uri.queryParameterNames.flatMap { paramKey ->
//        uri.getQueryParameters(paramKey).mapNotNull { paramValue ->
//            if (paramKey == key && paramValue == value) null else "$paramKey=$paramValue"
//        }
//    }
//
//    val baseUrl = this.substringBefore("?")
//    return if (newParams.isEmpty()) baseUrl else "$baseUrl?${newParams.joinToString("&")}"
//}
fun String.removeParameterFromUrl(key: String, value: String): String {
    val query = this.substringAfter("?", "")
    if (query.isEmpty()) return this

    val newQuery = query.split("&")
        .filter { it != "$key=$value" }
        .joinToString("&")

    return this.substringBefore("?") + if (newQuery.isNotEmpty()) "?$newQuery" else ""
}
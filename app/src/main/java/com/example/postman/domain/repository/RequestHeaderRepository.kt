package com.example.postman.domain.repository

interface RequestHeaderRepository {
    fun addHeader(key: String, value: String)
    fun removeHeader(key: String, value: String)
    fun clearHeaders()
    fun getHeaders(): List<Pair<String, String>>
}
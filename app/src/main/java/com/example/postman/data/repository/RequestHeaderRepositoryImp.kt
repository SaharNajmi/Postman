package com.example.postman.data.repository

import com.example.postman.domain.repository.RequestHeaderRepository

class RequestHeaderRepositoryImp : RequestHeaderRepository {
    private val headers = mutableListOf<Pair<String, String>>()

    override fun addHeader(key: String, value: String) {
        if (key.isBlank() || value.isBlank())
            return
        val modifiedValue = if (key.equals("Authorization", ignoreCase = true)) {
            headers.removeIf { it.first.equals("Authorization", ignoreCase = true) }
            if (value.startsWith("Bearer ")) {
                value
            } else "Bearer $value"
        } else {
            value
        }
        headers.add(Pair(key, modifiedValue))
    }

    override fun removeHeader(key: String, value: String) {
        headers.removeIf { it.first == key && it.second == value }
    }

    override fun clearHeaders() {
        headers.clear()
    }

    override fun getHeaders(): List<Pair<String, String>> = headers
}
package com.example.postman.domain.repository

import io.ktor.client.statement.HttpResponse

interface ApiService {
    suspend fun sendRequest(
        method: String,
        url: String,
        headers: List<Pair<String, String>>? = null,
        parameters: List<Pair<String, String>>? = null,
        body: Any? = null
    ): HttpResponse
}
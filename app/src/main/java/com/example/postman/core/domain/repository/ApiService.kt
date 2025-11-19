package com.example.postman.core.domain.repository

import com.example.postman.core.KeyValueList
import com.example.postman.core.domain.models.ApiResponse

interface ApiService {
    suspend fun sendRequest(
        method: String,
        url: String,
        headers: KeyValueList? = null,
        parameters: KeyValueList? = null,
        body: Any? = null,
    ): ApiResponse
}
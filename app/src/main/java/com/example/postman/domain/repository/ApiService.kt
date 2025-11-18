package com.example.postman.domain.repository

import com.example.postman.core.KeyValueList
import com.example.postman.domain.model.ApiResponse

interface ApiService {
    suspend fun sendRequest(
        method: String,
        url: String,
        headers: KeyValueList? = null,
        parameters: KeyValueList? = null,
        body: Any? = null,
    ): ApiResponse
}
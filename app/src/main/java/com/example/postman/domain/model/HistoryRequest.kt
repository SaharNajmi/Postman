package com.example.postman.domain.model

import com.example.postman.presentation.MethodName

data class HistoryRequest(
    val id: Int = 0,
    val requestUrl: String,
    val methodOption: MethodName,
    val response: String
)
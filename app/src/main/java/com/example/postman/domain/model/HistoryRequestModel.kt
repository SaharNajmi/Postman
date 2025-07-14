package com.example.postman.domain.model

import com.example.postman.presentation.MethodName
import java.time.LocalDate

data class HistoryRequestModel(
    val id: Int = 0,
    val requestUrl: String,
    val methodOption: MethodName,
    val response: String,
    val createdAt: LocalDate = LocalDate.now(),
    val statusCode: Int,
)
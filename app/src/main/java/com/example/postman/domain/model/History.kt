package com.example.postman.domain.model

import com.example.postman.common.utils.MethodName
import java.time.LocalDate

data class History(
    val id: Int = 0,
    val requestUrl: String,
    val methodOption: MethodName,
    val response: String,
    val createdAt: LocalDate = LocalDate.now(),
    val statusCode: Int,
)
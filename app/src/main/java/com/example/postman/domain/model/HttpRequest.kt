package com.example.postman.domain.model

import com.example.postman.common.utils.MethodName
import java.time.LocalDate

data class HttpRequest(
    val id: Int = 0,
    val requestUrl: String,
    val methodOption: MethodName,
    val createdAt: LocalDate = LocalDate.now(),
)

data class HttpResponse(
    val response: String,
    val statusCode: Int,
)

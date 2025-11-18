package com.example.postman.domain.models

import androidx.compose.ui.graphics.ImageBitmap
import com.example.postman.core.models.HttpMethod
import com.example.postman.core.KeyValueList
import java.time.LocalDate
import java.util.UUID

data class Collection(
    val collectionId: String = UUID.randomUUID().toString(),
    val collectionName: String = "New Collection",
    val requests: List<Request>? = null,
    val isExpanded: Boolean = false,
)

data class Request(
    val id: Int = 0,
    val requestUrl: String? = null,
    val httpMethod: HttpMethod = HttpMethod.GET,
    val requestName: String = "$httpMethod New Request",
    val createdAt: LocalDate = LocalDate.now(),
    val response: String = "",
    val imageResponse: ImageBitmap? = null,
    val statusCode: Int? = null,
    val body: String? = null,
    val headers: KeyValueList? = null,
)
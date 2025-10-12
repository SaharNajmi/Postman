package com.example.postman.domain.model

import androidx.compose.ui.graphics.ImageBitmap
import com.example.postman.common.utils.MethodName
import java.time.LocalDate
import java.util.UUID

data class Collection(
    val collectionId: String = UUID.randomUUID().toString(),
    val collectionName: String = "New Collection",
    val requests: List<Request>? = null
)

data class Request(
    val id: Int = 0,
    val requestUrl: String? = null,
    val methodOption: MethodName = MethodName.GET,
    val requestName: String = "$methodOption New Request",
    val createdAt: LocalDate = LocalDate.now(),
    val response: String = "",
    val imageResponse: ImageBitmap? = null,
    val statusCode: Int? = null,
    val body: String? = null,
    val headers: List<Pair<String, String>>? = null
)
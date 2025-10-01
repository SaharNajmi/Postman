package com.example.postman.domain.model

import androidx.compose.ui.graphics.ImageBitmap
import com.example.postman.common.utils.MethodName
import java.time.LocalDate

data class Collection(
    val id: Int = 0,
    val collectionId: Int = 0,
    val collectionName: String = "New Collection",
    val requestName: String = "New Request",
    val requestUrl: String = "",
    val methodOption: MethodName = MethodName.GET,
    val createdAt: LocalDate = LocalDate.now(),
    val response: String = "",
    val imageResponse: ImageBitmap? = null,
    val statusCode: Int? = null,
    val body: String? = null,
    val headers: List<Pair<String, String>>? = null
)
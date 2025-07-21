package com.example.postman.domain.model

import androidx.compose.ui.graphics.ImageBitmap
import com.example.postman.common.utils.MethodName
import java.time.LocalDate

data class History(
    val id: Int = 0,
    val requestUrl: String,
    val methodOption: MethodName,
    val createdAt: LocalDate = LocalDate.now(),
    val response: String,
    val imageResponse: ImageBitmap? = null,
    val statusCode: Int,
    val body: String? = null,
    val headers: Map<String, String>? = null
)
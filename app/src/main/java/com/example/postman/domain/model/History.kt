package com.example.postman.domain.model

import androidx.compose.ui.graphics.ImageBitmap
import com.example.postman.common.utils.HttpMethod
import java.time.LocalDate

data class History(
    val id: Int = 0,
    val requestUrl: String,
    val httpMethod: HttpMethod = HttpMethod.GET,
    val createdAt: LocalDate = LocalDate.now(),
    val response: String = "",
    val imageResponse: ImageBitmap? = null,
    val statusCode: Int? = null,
    val body: String? = null,
    val headers: List<Pair<String, String>>? = null
)
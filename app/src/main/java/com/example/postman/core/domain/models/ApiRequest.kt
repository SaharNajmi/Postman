package com.example.postman.core.domain.models

import androidx.compose.ui.graphics.ImageBitmap
import com.example.postman.core.extensions.mapStringToKeyValuePairs
import com.example.postman.core.models.HttpMethod
import com.example.postman.core.KeyValueList
import java.time.LocalDate

data class ApiRequest(
    val id: Int = 0,
    val requestUrl: String = "",
    val httpMethod: HttpMethod = HttpMethod.GET,
    val body: String? = null,
    val headers: KeyValueList? = null,
    val createdAt: LocalDate = LocalDate.now(),
) {
    val params: KeyValueList?
        get() = requestUrl.mapStringToKeyValuePairs()
    val baseUrl: String
        get() = requestUrl.substringBefore("?")
}

data class ApiResponse(
    val response: String,
    val statusCode: Int? = null,
    val imageResponse: ImageBitmap? = null,
)
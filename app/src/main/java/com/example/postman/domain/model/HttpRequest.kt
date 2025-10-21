package com.example.postman.domain.model

import androidx.compose.ui.graphics.ImageBitmap
import com.example.postman.common.extensions.mapStringToKeyValuePairs
import com.example.postman.common.utils.HttpMethod
import java.time.LocalDate

data class HttpRequest(
    val id: Int = 0,
    val requestUrl: String = "",
    val httpMethod: HttpMethod = HttpMethod.GET,
    val body: String? = null,
    val headers: List<Pair<String, String>>? = null,
    val createdAt: LocalDate = LocalDate.now(),
) {
    val params: List<Pair<String, String>>?
        get() = requestUrl.mapStringToKeyValuePairs()
    val baseUrl: String
        get()=requestUrl.substringBefore("?")
}

data class HttpResult(
    val response: String,
    val statusCode: Int? = null,
    val imageResponse: ImageBitmap? = null
)
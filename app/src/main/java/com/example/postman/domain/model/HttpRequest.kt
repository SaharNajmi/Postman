package com.example.postman.domain.model

import androidx.compose.ui.graphics.ImageBitmap
import com.example.postman.common.utils.MethodName
import java.time.LocalDate

data class HttpRequest(
    val id: Int = 0,
    val requestUrl: String = "",
    val methodOption: MethodName = MethodName.GET,
    val body: String? = null,
    val headers: Map<String, String>? = null,
    val createdAt: LocalDate = LocalDate.now(),
)

data class HttpResponse(
    val response: String,
    val statusCode: Int? = null,
    val imageResponse: ImageBitmap? = null
)


//data class HttpResponseBody(val stringBody: String = "", val imageBody: ImageBitmap? = null)
package com.example.postman.domain.model

import androidx.compose.ui.graphics.ImageBitmap
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
    val imageResponse: ImageBitmap? = null
)


//data class HttpResponseBody(val stringBody: String = "", val imageBody: ImageBitmap? = null)
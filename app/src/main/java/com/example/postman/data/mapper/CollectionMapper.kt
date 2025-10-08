package com.example.postman.data.mapper

import com.example.postman.domain.model.HttpRequest
import com.example.postman.domain.model.HttpResult
import com.example.postman.domain.model.Request

object CollectionMapper {
    fun Request.toHttpRequest(): HttpRequest =
        HttpRequest(
            requestUrl = requestUrl ?: "",
            methodOption = methodOption,
            body = body,
            headers = headers
        )


    fun Request.toHttpResponse(): HttpResult =
        HttpResult(
            response = response,
            statusCode = statusCode,
            imageResponse = imageResponse
        )
}
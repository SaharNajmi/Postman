package com.example.postman.data.mapper

import com.example.postman.domain.model.HttpRequest
import com.example.postman.domain.model.HttpResult
import com.example.postman.domain.model.Request

object CollectionMapper {
    fun Request.toHttpRequest(): HttpRequest =
        HttpRequest(
            id = id,
            requestUrl = requestUrl ?: "",
            httpMethod = httpMethod,
            body = body,
            headers = headers
        )


    fun Request.toHttpResponse(): HttpResult =
        HttpResult(
            response = response,
            statusCode = statusCode,
            imageResponse = imageResponse
        )

    fun httpRequestToRequest(
        httpRequest: HttpRequest,
        httpResult: HttpResult,
    ): Request =
        Request(
            id = httpRequest.id,
            requestUrl = httpRequest.requestUrl,
            httpMethod = httpRequest.httpMethod,
            createdAt = httpRequest.createdAt,
            response = httpResult.response,
            statusCode = httpResult.statusCode,
            imageResponse = httpResult.imageResponse,
            body = httpRequest.body,
            headers = httpRequest.headers
        )
}
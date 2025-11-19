package com.example.postman.home.domain

import com.example.postman.domain.models.ApiRequest
import com.example.postman.domain.models.ApiResponse
import com.example.postman.domain.models.Request

object CollectionMapper {
    fun Request.toHttpRequest(): ApiRequest =
        ApiRequest(
            id = id,
            requestUrl = requestUrl ?: "",
            httpMethod = httpMethod,
            body = body,
            headers = headers
        )


    fun Request.toHttpResponse(): ApiResponse =
        ApiResponse(
            response = response,
            statusCode = statusCode,
            imageResponse = imageResponse
        )

    fun httpRequestToRequest(
        apiRequest: ApiRequest,
        apiResponse: ApiResponse,
    ): Request =
        Request(
            id = apiRequest.id,
            requestUrl = apiRequest.requestUrl,
            httpMethod = apiRequest.httpMethod,
            createdAt = apiRequest.createdAt,
            response = apiResponse.response,
            statusCode = apiResponse.statusCode,
            imageResponse = apiResponse.imageResponse,
            body = apiRequest.body,
            headers = apiRequest.headers
        )
}
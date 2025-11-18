package com.example.postman.data.mapper

import com.example.postman.domain.models.History
import com.example.postman.domain.models.ApiRequest
import com.example.postman.domain.models.ApiResponse

object HistoryMapper {
    fun History.toHttpRequest(): ApiRequest =
        ApiRequest(
            id = id,
            requestUrl = requestUrl,
            httpMethod = httpMethod,
            body = body,
            headers = headers
        )


    fun History.toHttpResponse(): ApiResponse =
        ApiResponse(
            response = response,
            statusCode = statusCode,
            imageResponse = imageResponse
        )


    fun httpRequestToHistory(apiRequest: ApiRequest, apiResponse: ApiResponse): History =
        History(
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
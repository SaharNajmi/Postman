package com.example.postman.data.mapper

import com.example.postman.domain.model.History
import com.example.postman.domain.model.HttpRequest
import com.example.postman.domain.model.HttpResult

object HistoryMapper {
    fun History.toHttpRequest(): HttpRequest =
        HttpRequest(
            id = id,
            requestUrl = requestUrl,
            methodOption = methodOption,
            body = body,
            headers = headers
        )


    fun History.toHttpResponse(): HttpResult =
        HttpResult(
            response = response,
            statusCode = statusCode,
            imageResponse = imageResponse
        )


    fun httpRequestToHistory(httpRequest: HttpRequest, httpResult: HttpResult): History =
        History(
            requestUrl = httpRequest.requestUrl,
            methodOption = httpRequest.methodOption,
            createdAt = httpRequest.createdAt,
            response = httpResult.response,
            statusCode = httpResult.statusCode,
            imageResponse = httpResult.imageResponse,
            body = httpRequest.body,
            headers = httpRequest.headers
        )
}
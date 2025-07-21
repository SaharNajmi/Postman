package com.example.postman.data.mapper

import com.example.postman.domain.model.History
import com.example.postman.domain.model.HttpRequest
import com.example.postman.domain.model.HttpResponse

object HistoryMapper {
    fun History.toHttpRequest(): HttpRequest =
        HttpRequest(
            requestUrl = requestUrl,
            methodOption = methodOption,
            body = body,
            headers = headers
        )


fun History.toHttpResponse(): HttpResponse =
    HttpResponse(
        response = response,
        statusCode = statusCode,
        imageResponse = imageResponse
    )


fun httpRequestToHistory(httpRequest: HttpRequest, httpResponse: HttpResponse): History =
    History(
        requestUrl = httpRequest.requestUrl,
        methodOption = httpRequest.methodOption,
        createdAt = httpRequest.createdAt,
        response = httpResponse.response,
        statusCode = httpResponse.statusCode,
        imageResponse = httpResponse.imageResponse,
        body = httpRequest.body,
        headers = httpRequest.headers
    )
}
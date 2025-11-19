package com.example.postman.history.data.mapper

import com.example.postman.collection.data.entity.RequestEntity
import com.example.postman.core.data.extensions.toByteArray
import com.example.postman.core.data.extensions.toImageBitmap
import com.example.postman.core.data.extensions.toLocalDate
import com.example.postman.core.data.extensions.toLong
import com.example.postman.history.data.entity.HistoryEntity
import com.example.postman.history.domain.model.History

fun History.toEntity(collectionId: String): RequestEntity {
    return RequestEntity(
        collectionId = collectionId,
        requestUrl = requestUrl,
        requestName = "$httpMethod $requestUrl",
        httpMethod = httpMethod,
        response = response,
        createdAt = createdAt.toLong(),
        statusCode = statusCode,
        imageResponse = imageResponse?.toByteArray(),
        body = body,
        headers = headers
    )
}

fun HistoryEntity.toDomain(): History {
    return History(
        id = id,
        requestUrl = requestUrl,
        httpMethod = this@toDomain.httpMethod,
        response = response,
        createdAt = createdAt.toLocalDate(),
        statusCode = statusCode,
        imageResponse = imageResponse?.toImageBitmap(),
        body = body,
        headers = headers
    )
}

fun History.toEntity(): HistoryEntity {
    return HistoryEntity(
        id = id,
        requestUrl = requestUrl,
        httpMethod = httpMethod,
        response = response,
        createdAt = createdAt.toLong(),
        statusCode = statusCode,
        imageResponse = imageResponse?.toByteArray(),
        body = body,
        headers = headers
    )
}
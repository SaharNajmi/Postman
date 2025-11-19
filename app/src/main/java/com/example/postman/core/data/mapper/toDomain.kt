package com.example.postman.core.data.mapper

import com.example.postman.collection.data.entity.CollectionEntity
import com.example.postman.collection.data.entity.RequestEntity
import com.example.postman.core.data.extensions.toByteArray
import com.example.postman.core.data.extensions.toImageBitmap
import com.example.postman.core.data.extensions.toLocalDate
import com.example.postman.core.data.extensions.toLong
import com.example.postman.core.domain.models.Collection
import com.example.postman.core.domain.models.History
import com.example.postman.core.domain.models.Request
import com.example.postman.history.data.entity.HistoryEntity

fun CollectionEntity.toDomain(requests: List<Request>): Collection {
    return Collection(
        collectionId = collectionId,
        collectionName = collectionName,
        requests = requests
    )
}

fun Collection.toEntity(): CollectionEntity {
    return CollectionEntity(
        collectionId = collectionId,
        collectionName = collectionName
    )
}

fun Request.toEntity(collectionId: String): RequestEntity {
    return RequestEntity(
        id = id,
        collectionId = collectionId,
        requestName = requestName,
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

fun Request.toEntity(
    collectionId: String,
    requestName: String,
): RequestEntity {
    return RequestEntity(
        id = id,
        collectionId = collectionId,
        requestName = requestName,
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

fun History.toRequestEntity(collectionId: String): RequestEntity {
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

fun RequestEntity.toDomain(): Request {
    return Request(
        id = id,
        requestName = requestName,
        requestUrl = requestUrl,
        httpMethod = httpMethod,
        response = response,
        imageResponse = imageResponse?.toImageBitmap(),
        createdAt = createdAt.toLocalDate(),
        statusCode = statusCode,
        body = body,
        headers = headers
    )
}
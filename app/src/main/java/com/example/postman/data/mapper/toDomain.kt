package com.example.postman.data.mapper

import com.example.postman.data.extensions.toByteArray
import com.example.postman.data.extensions.toImageBitmap
import com.example.postman.data.extensions.toLocalDate
import com.example.postman.data.extensions.toLong
import com.example.postman.data.db.entities.CollectionEntity
import com.example.postman.data.db.entities.HistoryEntity
import com.example.postman.data.db.entities.RequestEntity
import com.example.postman.domain.models.Collection
import com.example.postman.domain.models.History
import com.example.postman.domain.models.Request

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
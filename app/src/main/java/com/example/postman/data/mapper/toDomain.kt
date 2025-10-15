package com.example.postman.data.mapper

import com.example.postman.common.extensions.toByteArray
import com.example.postman.common.extensions.toImageBitmap
import com.example.postman.common.extensions.toLocalDate
import com.example.postman.common.extensions.toLong
import com.example.postman.data.local.entity.CollectionEntity
import com.example.postman.data.local.entity.HistoryEntity
import com.example.postman.data.local.entity.RequestEntity
import com.example.postman.domain.model.Collection
import com.example.postman.domain.model.History
import com.example.postman.domain.model.Request

fun CollectionEntity.toDomain(requests: List<RequestEntity>): Collection {
    return Collection(
        collectionId = collectionId,
        collectionName = collectionName,
        requests = requests.map { request ->
            Request(
                id = request.id,
                requestName = request.requestName,
                requestUrl = request.requestUrl,
                methodOption = request.methodOption,
                response = request.response,
                createdAt = request.createdAt.toLocalDate(),
                statusCode = request.statusCode,
                body = request.body,
                headers = request.headers
            )
        }
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
        methodOption = methodOption,
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
        methodOption = methodOption,
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
        requestName = "$methodOption $requestUrl",
        methodOption = methodOption,
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
        methodOption = methodOption,
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
        methodOption = methodOption,
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
        methodOption = methodOption,
        response = response,
        imageResponse = imageResponse?.toImageBitmap(),
        createdAt = createdAt.toLocalDate(),
        statusCode = statusCode,
        body = body,
        headers = headers
    )
}
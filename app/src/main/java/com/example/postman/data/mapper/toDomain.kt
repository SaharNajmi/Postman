package com.example.postman.data.mapper

import com.example.postman.common.extensions.toByteArray
import com.example.postman.common.extensions.toImageBitmap
import com.example.postman.data.local.entity.CollectionEntity
import com.example.postman.data.local.entity.HistoryEntity
import com.example.postman.data.local.entity.RequestEntity
import com.example.postman.domain.model.Collection
import com.example.postman.domain.model.History
import com.example.postman.domain.model.Request
import java.time.Instant
import java.time.ZoneId

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
                createdAt = Instant.ofEpochMilli(request.createdAt)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate(),
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
        createdAt = createdAt.atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli(),
        statusCode = statusCode,
        body = body,
        headers = headers
    )
}

fun History.toRequestEntity(collectionId: String): RequestEntity {
    return RequestEntity(
        collectionId = collectionId,
        requestUrl = requestUrl,
        requestName = requestUrl,
        methodOption = methodOption,
        response = response,
        createdAt = createdAt
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli(),
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
        createdAt = Instant.ofEpochMilli(createdAt)
            .atZone(ZoneId.systemDefault())
            .toLocalDate(),
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
        createdAt = createdAt
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli(),
        statusCode = statusCode,
        imageResponse = imageResponse?.toByteArray(),
        body = body,
        headers = headers
    )
}
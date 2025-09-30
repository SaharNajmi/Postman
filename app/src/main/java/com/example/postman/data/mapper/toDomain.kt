package com.example.postman.data.mapper

import com.example.postman.common.extensions.toByteArray
import com.example.postman.common.extensions.toImageBitmap
import com.example.postman.data.local.entity.CollectionEntity
import com.example.postman.data.local.entity.HistoryEntity
import com.example.postman.domain.model.Collection
import com.example.postman.domain.model.History
import java.time.Instant
import java.time.ZoneId


fun CollectionEntity.toDomain(): Collection {
    return Collection(
        id = id,
        collectionName = collectionName,
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

fun History.toCollection(collectionName: String): Collection {
    return Collection(
        requestUrl = requestUrl,
        collectionName = collectionName,
        methodOption = methodOption,
        response = response,
        createdAt = createdAt,
        statusCode = statusCode,
        imageResponse = imageResponse,
        body = body,
        headers = headers
    )
}

fun Collection.toEntity(): CollectionEntity {
    return CollectionEntity(
        id = id,
        collectionName = collectionName,
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
package com.example.postman.data.mapper

import com.example.postman.data.local.entity.HistoryEntity
import com.example.postman.domain.model.History
import java.time.Instant
import java.time.ZoneId

fun HistoryEntity.toDomain(): History {
    return History(
        id = id,
        requestUrl = requestUrl,
        methodOption = methodOption,
        response = response,
        createdAt = Instant.ofEpochMilli(createdAt)
            .atZone(ZoneId.systemDefault())
            .toLocalDate(),
        statusCode = statusCode
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
        statusCode = statusCode
    )
}
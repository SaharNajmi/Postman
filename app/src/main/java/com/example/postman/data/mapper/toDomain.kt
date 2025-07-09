package com.example.postman.data.mapper

import com.example.postman.data.local.entity.HistoryRequestEntity
import com.example.postman.domain.model.HistoryRequestModel
import java.time.Instant
import java.time.ZoneId

fun HistoryRequestEntity.toDomain(): HistoryRequestModel {
    return HistoryRequestModel(
        id = id,
        requestUrl = requestUrl,
        methodOption = methodOption,
        response = response,
        createdAt=Instant.ofEpochMilli(createdAt)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    )
}

fun HistoryRequestModel.toEntity(): HistoryRequestEntity {
    return HistoryRequestEntity(
        id = id,
        requestUrl = requestUrl,
        methodOption = methodOption,
        response = response,
        createdAt= createdAt
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    )
}
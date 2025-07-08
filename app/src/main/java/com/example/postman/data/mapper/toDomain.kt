package com.example.postman.data.mapper

import com.example.postman.data.local.entity.HistoryRequestEntity
import com.example.postman.domain.model.HistoryRequest

fun HistoryRequestEntity.toDomain(): HistoryRequest {
    return HistoryRequest(
        id = id,
        requestUrl = requestUrl,
        methodOption = methodOption,
        response = response
    )
}

fun HistoryRequest.toEntity(): HistoryRequestEntity {
    return HistoryRequestEntity(
        id = id,
        requestUrl = requestUrl,
        methodOption = methodOption,
        response = response
    )
}
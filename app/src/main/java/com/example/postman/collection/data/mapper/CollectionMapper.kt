package com.example.postman.collection.data.mapper

import com.example.postman.collection.data.entity.CollectionEntity
import com.example.postman.collection.domain.model.Collection
import com.example.postman.collection.domain.model.Request


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
package com.example.postman.collection.domain.repository

import com.example.postman.core.domain.models.Collection
import com.example.postman.core.domain.models.Request

interface CollectionRepository {
    suspend fun insertCollection(collection: Collection)

    suspend fun getAllCollections(): List<Collection>

    suspend fun getRequestName(requestId: Int): String

    suspend fun updateCollection(collection: Collection)

    suspend fun updateCollectionRequest(collectionId: String, request: Request)

    suspend fun deleteCollection(collectionId: String)

    suspend fun insertRequestToCollection(collectionId: String, request: Request)

    suspend fun getCollectionRequests(collectionId: String): List<Request>

    suspend fun getCollectionRequest(requestId: Int): Request

    suspend fun deleteRequestFromCollection(requestId: Int)

    suspend fun changeRequestName(requestId: Int, requestName: String)
}
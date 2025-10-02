package com.example.postman.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.postman.data.local.entity.CollectionEntity
import com.example.postman.data.local.entity.RequestEntity

@Dao
interface CollectionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCollection(collection: CollectionEntity)

    @Query("SELECT * FROM collections")
    fun getAllCollections(): List<CollectionEntity>

    @Update
    suspend fun updateCollection(collection: CollectionEntity)

    @Query("DELETE FROM collections WHERE collectionId = :collectionId")
    suspend fun deleteCollection(collectionId: String)

    @Insert
     fun insertRequestToCollection(request: RequestEntity)

    @Query("SELECT * FROM requests WHERE collectionId = :collectionId")
    fun getRequestsForCollection(collectionId: String): List<RequestEntity>

    @Query("DELETE FROM requests WHERE id = :requestId")
    suspend fun deleteRequestFromCollection(requestId: Int)

    @Query("DELETE FROM requests WHERE id IN (:requestIds)")
    fun deleteRequestsFromCollection(requestIds: List<Int>)
}

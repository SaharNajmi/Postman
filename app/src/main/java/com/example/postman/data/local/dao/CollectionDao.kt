package com.example.postman.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.postman.data.local.entity.CollectionEntity

@Dao
interface CollectionDao {
    @Query("SELECT * FROM collections ORDER by id DESC")
    fun getAllCollections(): List<CollectionEntity>

    @Insert
    fun insertRequestToCollections(collection: CollectionEntity)

    @Update
    fun updateCollectionItem(collection: CollectionEntity)

    @Query("DELETE FROM collections WHERE id=:requestId")
    fun deleteRequestFromCollection(requestId: Int)

    @Query("DELETE FROM collections WHERE id in (:requestIds)")
    fun deleteRequestsFromCollection(requestIds: List<Int>)

    @Query("SELECT * FROM collections WHERE id= :requestId ")
    fun getRequest(requestId: Int): CollectionEntity

}
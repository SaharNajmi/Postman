package com.example.postman.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.postman.data.local.entity.HistoryRequestEntity

@Dao
interface HistoryRequestDao {
    @Query("SELECT * FROM histories")
    suspend fun getAllHistories(): List<HistoryRequestEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHistoryRequest(history: HistoryRequestEntity)

    @Update
    suspend fun updateHistoryRequest(history: HistoryRequestEntity)

    @Delete
    suspend fun deleteHistoryRequest(history: HistoryRequestEntity)

    @Query("SELECT * FROM histories WHERE id= :historyId ")
    fun getHistoryRequest(historyId: Int): HistoryRequestEntity

}
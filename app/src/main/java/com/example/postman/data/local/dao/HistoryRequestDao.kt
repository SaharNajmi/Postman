package com.example.postman.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.postman.data.local.entity.HistoryEntity

@Dao
interface HistoryRequestDao {
    @Query("SELECT * FROM histories ORDER by id DESC")
    suspend fun getAllHistories(): List<HistoryEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHistoryRequest(history: HistoryEntity)

    @Update
    suspend fun updateHistoryRequest(history: HistoryEntity)

    @Query("DELETE FROM histories WHERE id = :historyId")
    suspend fun deleteHistoryRequest(historyId: Int)

    @Query("SELECT * FROM histories WHERE id= :historyId ")
    fun getHistoryRequest(historyId: Int): HistoryEntity

}
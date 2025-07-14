package com.example.postman.data.local.appDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.postman.data.local.dao.HistoryRequestDao
import com.example.postman.data.local.entity.HistoryEntity

@Database(entities = [HistoryEntity::class], version = 3, exportSchema = false)
abstract class RoomDatabase: RoomDatabase() {
    abstract fun historyRequestDao(): HistoryRequestDao
}

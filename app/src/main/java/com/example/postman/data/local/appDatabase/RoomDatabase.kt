package com.example.postman.data.local.appDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.postman.data.local.dao.HistoryRequestDao
import com.example.postman.data.local.entity.HistoryEntity

@Database(entities = [HistoryEntity::class], version = 4, exportSchema = false)
@TypeConverters()
abstract class RoomDatabase : RoomDatabase() {
    abstract fun historyRequestDao(): HistoryRequestDao
}

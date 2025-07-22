package com.example.postman.data.local.appDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.postman.data.local.dao.HistoryRequestDao
import com.example.postman.data.local.entity.HistoryEntity
import com.example.postman.data.mapper.Converters

@Database(entities = [HistoryEntity::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun historyRequestDao(): HistoryRequestDao
}

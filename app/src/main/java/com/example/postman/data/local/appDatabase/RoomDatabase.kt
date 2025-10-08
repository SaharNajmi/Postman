package com.example.postman.data.local.appDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.postman.data.local.dao.CollectionDao
import com.example.postman.data.local.dao.HistoryRequestDao
import com.example.postman.data.local.entity.CollectionEntity
import com.example.postman.data.local.entity.HistoryEntity
import com.example.postman.data.local.entity.RequestEntity
import com.example.postman.data.mapper.Converters

@Database(
    entities = [HistoryEntity::class, CollectionEntity::class, RequestEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun historyRequestDao(): HistoryRequestDao
    abstract fun collectionDao(): CollectionDao
}

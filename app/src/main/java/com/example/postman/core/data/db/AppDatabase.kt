package com.example.postman.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.postman.collection.data.dao.CollectionDao
import com.example.postman.history.data.dao.HistoryRequestDao
import com.example.postman.collection.data.entity.CollectionEntity
import com.example.postman.history.data.entity.HistoryEntity
import com.example.postman.collection.data.entity.RequestEntity

@Database(
    entities = [HistoryEntity::class, CollectionEntity::class, RequestEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyRequestDao(): HistoryRequestDao
    abstract fun collectionDao(): CollectionDao
}
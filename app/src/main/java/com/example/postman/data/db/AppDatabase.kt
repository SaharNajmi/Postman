package com.example.postman.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.postman.data.db.Converters
import com.example.postman.data.db.dao.CollectionDao
import com.example.postman.data.db.dao.HistoryRequestDao
import com.example.postman.data.db.entities.CollectionEntity
import com.example.postman.data.db.entities.HistoryEntity
import com.example.postman.data.db.entities.RequestEntity

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
package com.example.postman.data.di

import android.content.Context
import androidx.room.Room
import com.example.postman.data.db.AppDatabase
import com.example.postman.data.db.dao.CollectionDao
import com.example.postman.data.db.dao.HistoryRequestDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "history_db")
            .fallbackToDestructiveMigration(true)
            .build()


    @Provides
    fun provideHistoryDao(db: AppDatabase): HistoryRequestDao =
        db.historyRequestDao()

    @Provides
    fun provideCollocationDao(db: AppDatabase): CollectionDao = db.collectionDao()
}
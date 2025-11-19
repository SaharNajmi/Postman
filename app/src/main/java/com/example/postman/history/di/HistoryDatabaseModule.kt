package com.example.postman.history.di

import com.example.postman.core.data.db.AppDatabase
import com.example.postman.history.data.dao.HistoryRequestDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object HistoryDatabaseModule {

    @Provides
    fun provideHistoryDao(db: AppDatabase): HistoryRequestDao =
        db.historyRequestDao()
}
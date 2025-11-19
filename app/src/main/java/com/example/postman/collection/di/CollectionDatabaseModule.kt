package com.example.postman.collection.di

import com.example.postman.collection.data.dao.CollectionDao
import com.example.postman.core.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CollectionDatabaseModule {

    @Provides
    fun provideCollocationDao(db: AppDatabase): CollectionDao = db.collectionDao()
}
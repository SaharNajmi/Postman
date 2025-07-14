package com.example.postman.data.di

import android.content.Context
import androidx.room.Room
import com.example.postman.data.local.appDatabase.RoomDatabase
import com.example.postman.data.local.dao.HistoryRequestDao
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
    fun provideDatabase(@ApplicationContext context: Context): RoomDatabase =
        Room.databaseBuilder(context, RoomDatabase::class.java, "history_db")
            .fallbackToDestructiveMigration(true)
            .build()


    @Provides
    fun provideHistoryDao(db: RoomDatabase): HistoryRequestDao =
        db.historyRequestDao()
}
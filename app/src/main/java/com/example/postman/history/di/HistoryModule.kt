package com.example.postman.history.di

import com.example.postman.core.data.di.IoDispatcher
import com.example.postman.history.data.dao.HistoryRequestDao
import com.example.postman.history.data.repository.HistoryRepositoryImp
import com.example.postman.history.domain.repository.HistoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher


@Module
@InstallIn(SingletonComponent::class)
object HistoryModule {

    @Provides
    fun provideHistoryRequestRepository(
        historyRequestDao: HistoryRequestDao,
        @IoDispatcher dispatcher: CoroutineDispatcher,
    ): HistoryRepository =
        HistoryRepositoryImp(historyRequestDao, dispatcher)

}
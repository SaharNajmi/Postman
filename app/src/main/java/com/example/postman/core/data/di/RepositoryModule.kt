package com.example.postman.core.data.di

import com.example.postman.collection.data.dao.CollectionDao
import com.example.postman.core.data.repository.ApiServiceImp
import com.example.postman.core.data.repository.CollectionRepositoryImp
import com.example.postman.core.data.repository.HistoryRepositoryImp
import com.example.postman.core.domain.repository.ApiService
import com.example.postman.core.domain.repository.CollectionRepository
import com.example.postman.core.domain.repository.HistoryRepository
import com.example.postman.history.data.dao.HistoryRequestDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideHistoryRequestRepository(
        historyRequestDao: HistoryRequestDao,
        @IoDispatcher dispatcher: CoroutineDispatcher,
    ): HistoryRepository =
        HistoryRepositoryImp(historyRequestDao, dispatcher)

    @Provides
    fun provideCollectionRepository(
        collectionDao: CollectionDao,
        @IoDispatcher dispatcher: CoroutineDispatcher,
    ): CollectionRepository =
        CollectionRepositoryImp(collectionDao, dispatcher)


    @Singleton
    @Provides
    fun provideApiService(httpClient: HttpClient): ApiService = ApiServiceImp(httpClient)

    @Singleton
    @Provides
    fun provideHttpClient() = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }
}
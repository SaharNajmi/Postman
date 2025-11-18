package com.example.postman.data.di

import com.example.postman.core.constants.NetworkConstants
import com.example.postman.data.db.dao.CollectionDao
import com.example.postman.data.db.dao.HistoryRequestDao
import com.example.postman.data.repository.ApiServiceImp
import com.example.postman.data.repository.CollectionRepositoryImp
import com.example.postman.data.repository.HistoryRepositoryImp
import com.example.postman.domain.repository.ApiService
import com.example.postman.domain.repository.CollectionRepository
import com.example.postman.domain.repository.HistoryRepository
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
    fun provideBaseUrl(): String {
        return NetworkConstants.BASE_URL
    }

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
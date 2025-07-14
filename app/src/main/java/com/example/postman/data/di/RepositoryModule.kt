package com.example.postman.data.di

import com.example.postman.common.constants.NetworkConstants
import com.example.postman.data.local.dao.HistoryRequestDao
import com.example.postman.data.remote.ApiService
import com.example.postman.domain.repository.ApiRepository
import com.example.postman.data.repository.ApiRepositoryImp
import com.example.postman.data.repository.HistoryRepositoryImp
import com.example.postman.domain.repository.HistoryRepository
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideBaseUrl(): String {
        return NetworkConstants.BASE_URL
    }

    @Provides
    fun provideHistoryRequestRepository(historyRequestDao: HistoryRequestDao): HistoryRepository =
        HistoryRepositoryImp(historyRequestDao)

    @Provides
    fun provideApiRepository(apiService: ApiService): ApiRepository =
        ApiRepositoryImp(apiService)

    @Provides
    fun provideRetrofit(baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

}
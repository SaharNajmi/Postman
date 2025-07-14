package com.example.postman.data.di

import com.example.postman.data.Constant
import com.example.postman.data.local.dao.HistoryRequestDao
import com.example.postman.data.remote.ApiService
import com.example.postman.data.repository.ApiRepository
import com.example.postman.data.repository.ApiRepositoryImp
import com.example.postman.data.repository.HistoryRequestRepositoryImp
import com.example.postman.domain.repository.HistoryRequestRepository
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
        return Constant.BASE_URL
    }

    @Provides
    fun provideHistoryRequestRepository(historyRequestDao: HistoryRequestDao): HistoryRequestRepository =
        HistoryRequestRepositoryImp(historyRequestDao)

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
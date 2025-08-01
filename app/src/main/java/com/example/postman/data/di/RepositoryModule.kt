package com.example.postman.data.di

import com.example.postman.common.constants.NetworkConstants
import com.example.postman.data.local.dao.HistoryRequestDao
import com.example.postman.data.remote.ApiService
import com.example.postman.data.remote.QueryParamsInterceptor
import com.example.postman.data.remote.RequestHeadersInterceptor
import com.example.postman.data.repository.ApiRepositoryImp
import com.example.postman.data.repository.HistoryRepositoryImp
import com.example.postman.data.repository.QueryParamsRepositoryImp
import com.example.postman.data.repository.RequestHeaderRepositoryImp
import com.example.postman.domain.repository.ApiRepository
import com.example.postman.domain.repository.HistoryRepository
import com.example.postman.domain.repository.QueryParamsRepository
import com.example.postman.domain.repository.RequestHeaderRepository
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
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
    fun provideHistoryRequestRepository(
        historyRequestDao: HistoryRequestDao,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): HistoryRepository =
        HistoryRepositoryImp(historyRequestDao, dispatcher)

    @Provides
    fun provideApiRepository(
        apiService: ApiService,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): ApiRepository =
        ApiRepositoryImp(apiService, dispatcher)


    @Provides
    @Singleton
    fun provideRequestHeaderRepository(): RequestHeaderRepository = RequestHeaderRepositoryImp()

    @Provides
    @Singleton
    fun provideQueryParamsRepository(): QueryParamsRepository = QueryParamsRepositoryImp()

    @Provides
    @Singleton
    fun provideRequestHeadersInterceptor(requestHeaderRepository: RequestHeaderRepository): RequestHeadersInterceptor =
        RequestHeadersInterceptor(requestHeaderRepository)


    @Provides
    @Singleton
    fun provideQueryParamsInterceptor(queryParamsRepository: QueryParamsRepository): QueryParamsInterceptor =
        QueryParamsInterceptor(queryParamsRepository)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        headersInterceptor: RequestHeadersInterceptor,
        paramsInterceptor: QueryParamsInterceptor,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(headersInterceptor)
            .addInterceptor(paramsInterceptor)
            .build()

    @Provides
    fun provideRetrofit(baseUrl: String, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

}
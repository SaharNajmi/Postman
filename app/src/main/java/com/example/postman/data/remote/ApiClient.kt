package com.example.postman.data.remote

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
        fun createApiService(): ApiService {
            return Retrofit.Builder()
                .baseUrl("https://placeholder.com/")
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
                .create(ApiService::class.java)
        }
}
package com.example.postman.data.remote

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HEAD
import retrofit2.http.OPTIONS
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Url

interface ApiService {
    @GET
    suspend fun getRequest(@Url url: String): Response<ResponseBody>

    @POST
    suspend fun postRequest(
        @Url url: String,
        @Body body: RequestBody? = null
    ): Response<ResponseBody>

    @DELETE
    suspend fun deleteRequest(@Url url: String): Response<ResponseBody>

    @PUT
    suspend fun putRequest(
        @Url url: String,
        @Body body: RequestBody? = null
    ): Response<ResponseBody>

    @PATCH
    suspend fun patchRequest(
        @Url url: String,
        @Body body: RequestBody? = null
    ): Response<ResponseBody>

    @HEAD
    suspend fun headRequest(@Url url: String): Response<ResponseBody>

    @OPTIONS
    suspend fun optionsRequest(@Url url: String): Response<ResponseBody>

}
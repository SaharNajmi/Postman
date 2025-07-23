package com.example.postman.data.remote

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HEAD
import retrofit2.http.HeaderMap
import retrofit2.http.OPTIONS
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Url

interface ApiService {
    @GET
    suspend fun getRequest(
        @HeaderMap headers: Map<String, String>? = null,
        @Url url: String): Response<ResponseBody>

    @POST
    suspend fun postRequest(
        @HeaderMap headers: Map<String, String>? = null,
        @Url url: String,
        @Body body: RequestBody? = null
    ): Response<ResponseBody>

    @DELETE
    suspend fun deleteRequest(
        @HeaderMap headers: Map<String, String>? = null,
        @Url url: String): Response<ResponseBody>

    @PUT
    suspend fun putRequest(
        @HeaderMap headers: Map<String, String>? = null,
        @Url url: String,
        @Body body: RequestBody? = null
    ): Response<ResponseBody>

    @PATCH
    suspend fun patchRequest(
        @HeaderMap headers: Map<String, String>? = null,
        @Url url: String,
        @Body body: RequestBody? = null
    ): Response<ResponseBody>

    @HEAD
    suspend fun headRequest(
        @HeaderMap headers: Map<String, String>? = null,
        @Url url: String): Response<ResponseBody>

    @OPTIONS
    suspend fun optionsRequest(
        @HeaderMap headers: Map<String, String>? = null,
        @Url url: String): Response<ResponseBody>

}
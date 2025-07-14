package com.example.postman.data.repository

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response

interface ApiRepository {
    suspend fun request(
        method: String,
        url: String,
        body: RequestBody? = null
    ): Response<ResponseBody>

//    suspend fun getRequest(url: String): Response<ResponseBody>
//
//    suspend fun postRequest(
//        url: String,
//        body: RequestBody? = null
//    )
//
//    suspend fun deleteRequest(url: String): Response<ResponseBody>
//
//    suspend fun putRequest(
//        url: String,
//        body: RequestBody? = null)
//
//    suspend fun patchRequest(
//        url: String,
//        body: RequestBody? = null
//    ): Response<ResponseBody>
//
//    suspend fun headRequest(url: String): Response<Void>
//
//    suspend fun optionsRequest(url: String): Response<Void>
}
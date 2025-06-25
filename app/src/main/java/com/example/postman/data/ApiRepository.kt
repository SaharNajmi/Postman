package com.example.postman.data

import okhttp3.RequestBody

interface ApiRepository {
    suspend fun request(
        method: String,
        url: String,
        body: RequestBody? = null
    ): Result<String>

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
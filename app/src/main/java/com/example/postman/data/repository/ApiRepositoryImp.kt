package com.example.postman.data.repository

import com.example.postman.data.remote.ApiService
import com.example.postman.presentation.MethodName
import okhttp3.RequestBody

class ApiRepositoryImp(val apiService: ApiService) : ApiRepository {
    override suspend fun request(
        method: String,
        url: String,
        body: RequestBody?
    ): Result<String> {
        return try {
            val response = when (method) {
                MethodName.GET.name -> apiService.getRequest(url)
                MethodName.POST.name -> apiService.postRequest(url, body)
                MethodName.PUT.name -> apiService.putRequest(url, body)
                MethodName.PATCH.name -> apiService.patchRequest(url, body)
                MethodName.DELETE.name -> apiService.deleteRequest(url)
                MethodName.HEAD.name -> apiService.headRequest(url)
                MethodName.OPTIONS.name -> apiService.optionsRequest(url)
                else -> {
                    throw IllegalArgumentException("unsupported method: $method")
                }
            }
            if (response.isSuccessful) {
                Result.success(response.body()?.string() ?: "Empty")
            } else {
                Result.failure(Exception("HTTP ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
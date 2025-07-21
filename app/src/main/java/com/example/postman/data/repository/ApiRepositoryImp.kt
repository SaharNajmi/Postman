package com.example.postman.data.repository

import com.example.postman.data.remote.ApiService
import com.example.postman.common.utils.MethodName
import com.example.postman.domain.repository.ApiRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response

class ApiRepositoryImp(
    private val apiService: ApiService,
    private val dispatcher: CoroutineDispatcher
) : ApiRepository {
    override suspend fun request(
        method: String,
        url: String,
        body: RequestBody?,
        headers: Map<String, String>?
    ): Response<ResponseBody> = withContext(dispatcher) {
        val response = when (method) {
            MethodName.GET.name -> apiService.getRequest(url)
            MethodName.POST.name -> apiService.postRequest(headers, url, body)
            MethodName.PUT.name -> apiService.putRequest(url, body)
            MethodName.PATCH.name -> apiService.patchRequest(url, body)
            MethodName.DELETE.name -> apiService.deleteRequest(url)
            MethodName.HEAD.name -> apiService.headRequest(url)
            MethodName.OPTIONS.name -> apiService.optionsRequest(url)
            else -> {
                throw IllegalArgumentException("unsupported method: $method")
            }
        }
        response
    }
}
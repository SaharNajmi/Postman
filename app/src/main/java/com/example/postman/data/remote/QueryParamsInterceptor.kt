package com.example.postman.data.remote

import com.example.postman.domain.repository.QueryParamsRepository
import com.example.postman.domain.repository.RequestHeaderRepository
import okhttp3.Interceptor
import okhttp3.Response

class QueryParamsInterceptor(
    private val params: QueryParamsRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val originUrl = originRequest.url
        val urlBuilder = originUrl.newBuilder()
        params.getParameters().forEach { (key, value) ->
            urlBuilder.addQueryParameter(key, value)
        }
        val newUrl = urlBuilder.build()
        val newRequest = originRequest.newBuilder().url(newUrl).build()
        return chain.proceed(newRequest)
    }
}
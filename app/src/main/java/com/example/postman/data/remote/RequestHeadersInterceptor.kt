package com.example.postman.data.remote

import com.example.postman.domain.repository.RequestHeaderRepository
import okhttp3.Interceptor
import okhttp3.Response

class RequestHeadersInterceptor(
    private val header: RequestHeaderRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        header.getHeaders().forEach { (key, value) ->
            requestBuilder.addHeader(key, value)
        }

        return chain.proceed(requestBuilder.build())
    }
}
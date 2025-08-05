package com.example.postman.data.repository

import com.example.postman.domain.repository.ApiService
import io.ktor.client.HttpClient
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod

class ApiServiceImp(
    private val client: HttpClient ,
) : ApiService {

    override suspend fun sendRequest(
        method: String,
        url: String,
        headers: List<Pair<String, String>>?,
        parameters: List<Pair<String, String>>?,
        body: Any?
    ): HttpResponse {
        val httpMethod = HttpMethod.parse(method)

        return client.request(url) {
            this.method = httpMethod
            url {
                parameters?.forEach { (key, value) ->
                    this.parameters.append(key, value)
                }
            }

            headers?.forEach { (key, value) ->
                this.headers.append(key, value)
            }

            if (body != null && httpMethod !in listOf(HttpMethod.Get, HttpMethod.Head)) {
                setBody(body)
            }
        }
    }
}
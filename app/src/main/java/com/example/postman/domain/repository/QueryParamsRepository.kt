package com.example.postman.domain.repository

interface QueryParamsRepository {
    fun addParameter(key: String, value: String)
    fun removeParameter(key: String, value: String)
    fun clearParameters()
    fun getParameters(): List<Pair<String, String>>
}
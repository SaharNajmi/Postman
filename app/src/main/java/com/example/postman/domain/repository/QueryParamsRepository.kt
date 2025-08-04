package com.example.postman.domain.repository

interface QueryParamsRepository {
    fun addParameter(key: String, value: String)
    fun updateParameter(newParams: List<Pair<String, String>>)
    fun removeParameter(key: String, value: String)
    fun clearParameters()
    fun getParameters(): List<Pair<String, String>>
}
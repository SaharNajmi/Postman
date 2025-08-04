package com.example.postman.data.repository

import com.example.postman.domain.repository.QueryParamsRepository

class QueryParamsRepositoryImp : QueryParamsRepository {
    private var params = mutableListOf<Pair<String, String>>()

    override fun addParameter(key: String, value: String) {
        if (key.isBlank() || value.isBlank())
            return
        params.add(Pair(key, value))
    }

    override fun updateParameter(newParams: List<Pair<String, String>>) {
        params = newParams.toMutableList()
    }

    override fun removeParameter(key: String, value: String) {
        params.removeIf { it.first == key && it.second == value }
    }

    override fun clearParameters() {
        params.clear()
    }

    override fun getParameters(): List<Pair<String, String>> = params
}
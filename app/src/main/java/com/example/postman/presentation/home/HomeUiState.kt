package com.example.postman.presentation.home

import com.example.postman.domain.model.HttpRequest
import com.example.postman.domain.model.HttpResult
import com.example.postman.presentation.base.Loadable

data class HomeUiState(
    val data: HttpRequest,
    val response: Loadable<HttpResult>? = null,
)

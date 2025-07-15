package com.example.postman.presentation.home

import com.example.postman.domain.model.HttpRequest
import com.example.postman.domain.model.HttpResponse
import com.example.postman.presentation.base.Loadable

data class HomeUiState(
    val data: HttpRequest,
    val response: Loadable<HttpResponse>? = null,
)

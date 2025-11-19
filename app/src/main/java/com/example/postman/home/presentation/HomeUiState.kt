package com.example.postman.home.presentation

import com.example.postman.core.domain.model.ApiRequest
import com.example.postman.core.domain.model.ApiResponse

data class HomeUiState(
    val data: ApiRequest,
    val response: Loadable<ApiResponse>,
)
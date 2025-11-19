package com.example.postman.home.presentation

import com.example.postman.core.domain.models.ApiRequest
import com.example.postman.core.domain.models.ApiResponse

data class HomeUiState(
    val data: ApiRequest,
    val response: Loadable<ApiResponse>,
)
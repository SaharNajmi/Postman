package com.example.postman.home.presentation

import com.example.postman.domain.models.ApiRequest
import com.example.postman.domain.models.ApiResponse
import com.example.postman.home.presentation.Loadable

data class HomeUiState(
    val data: ApiRequest,
    val response: Loadable<ApiResponse>,
)
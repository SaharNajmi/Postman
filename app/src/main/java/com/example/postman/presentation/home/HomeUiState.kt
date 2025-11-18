package com.example.postman.presentation.home

import com.example.postman.domain.models.ApiRequest
import com.example.postman.domain.models.ApiResponse
import com.example.postman.presentation.home.Loadable

data class HomeUiState(
    val data: ApiRequest,
    val response: Loadable<ApiResponse>,
)

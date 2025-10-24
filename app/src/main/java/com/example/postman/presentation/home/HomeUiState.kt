package com.example.postman.presentation.home

import com.example.postman.domain.model.ApiRequest
import com.example.postman.domain.model.ApiResponse
import com.example.postman.presentation.base.Loadable

data class HomeUiState(
    val data: ApiRequest,
    val response: Loadable<ApiResponse>,
)

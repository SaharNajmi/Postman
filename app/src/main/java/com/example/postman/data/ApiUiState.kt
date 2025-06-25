package com.example.postman.data

sealed class ApiUiState {
    object Loading : ApiUiState()
    data class Success(val data: String) : ApiUiState()
    data class Error(val message: String) : ApiUiState()
}
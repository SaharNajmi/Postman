package com.example.postman.data

sealed class ApiUiState<out U> {
    object Idle : ApiUiState<Nothing>()
    object Loading : ApiUiState<Nothing>()
    data class Success<T>(val data: T) : ApiUiState<T>()
    data class Error(val message: String) : ApiUiState<Nothing>()
}
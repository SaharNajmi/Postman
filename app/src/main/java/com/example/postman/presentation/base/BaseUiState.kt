package com.example.postman.presentation.base

sealed class BaseUiState<out U> {
    object Idle : BaseUiState<Nothing>()
    object Loading : BaseUiState<Nothing>()
    data class Success<T>(val data: T) : BaseUiState<T>()
    data class Error(val message: String) : BaseUiState<Nothing>()
}
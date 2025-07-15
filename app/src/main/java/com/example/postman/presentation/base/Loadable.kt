package com.example.postman.presentation.base

sealed class Loadable<out U> {
    object Loading : Loadable<Nothing>()
    data class Success<T>(val data: T) : Loadable<T>()
    data class Error(val message: String) : Loadable<Nothing>()
    data class NetworkError(val message: String) : Loadable<Nothing>()
}
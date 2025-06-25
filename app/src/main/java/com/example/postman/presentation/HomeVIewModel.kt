package com.example.postman.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postman.data.ApiRepository
import com.example.postman.data.ApiUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: ApiRepository) : ViewModel() {

    private val _response = MutableStateFlow<ApiUiState>(ApiUiState.Loading)
    val response: StateFlow<ApiUiState> = _response.asStateFlow()
    fun request(
        methodName: MethodName,
        url: String,
        body: String? = null
    ) {
        viewModelScope.launch {
            val result = repository.request(methodName.name, url)
            _response.value = result.fold(
                onSuccess = { ApiUiState.Success(it) },
                onFailure = { ApiUiState.Error(it.message ?: "Unknown error") }
            )
        }
    }
}
package com.example.postman.presentation.home

import com.example.postman.common.utils.MethodName

data class HomeCallbacks(
    val onSendRequestClick: () -> Unit,
    val onBodyChanged: (String) -> Unit,
    val onAddHeader: (String, String) -> Unit,
    val onRemoveHeader: (String, String) -> Unit,
    val onAddParameter: (String, String) -> Unit,
    val onRemoveParameter: (String, String) -> Unit,
    val onMethodNameChanged: (MethodName) -> Unit,
    val onRequestUrlChanged: (String) -> Unit,
    val onClearDataClick: () -> Unit,
    val onNavigateToHistory: () -> Unit,
    val onNavigateToCollection: () -> Unit,
)
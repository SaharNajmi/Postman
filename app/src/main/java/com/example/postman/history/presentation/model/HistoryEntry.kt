package com.example.postman.history.presentation.model

import com.example.postman.history.domain.model.History

data class HistoryEntry(
    val dateCreated: String,
    val histories: List<History>
)
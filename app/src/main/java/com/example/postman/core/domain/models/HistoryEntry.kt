package com.example.postman.core.domain.models

data class HistoryEntry(
    val dateCreated: String,
    val histories: List<History>
)
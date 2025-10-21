package com.example.postman.domain.model

data class HistoryEntry(
    val dateCreated: String,
    val histories: List<History>
)
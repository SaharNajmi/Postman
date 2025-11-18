package com.example.postman.domain.models

data class HistoryEntry(
    val dateCreated: String,
    val histories: List<History>
)
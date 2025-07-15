package com.example.postman.domain.repository

import com.example.postman.domain.model.History

interface HistoryRepository {
    suspend fun getAllHistories(): List<History>

    suspend fun insertHistoryRequest(history: History)

    suspend fun updateHistoryRequest(historyItem: History)

    suspend fun deleteHistoryRequest(historyId: Int)

    suspend fun getHistoryRequest(historyId: Int): History
}
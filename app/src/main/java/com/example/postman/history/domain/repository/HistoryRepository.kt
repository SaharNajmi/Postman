package com.example.postman.history.domain.repository

import com.example.postman.core.domain.models.History

interface HistoryRepository {
    suspend fun getAllHistories(): List<History>

    suspend fun insertHistoryRequest(history: History)

    suspend fun updateHistoryRequest(historyItem: History)

    suspend fun deleteHistoryRequest(historyId: Int)

    suspend fun deleteHistoriesRequest(ids: List<Int>)

    suspend fun getHistoryRequest(historyId: Int): History
}
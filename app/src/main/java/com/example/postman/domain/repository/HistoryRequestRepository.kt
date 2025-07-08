package com.example.postman.domain.repository

import com.example.postman.domain.model.HistoryRequest
import kotlinx.coroutines.flow.Flow

interface HistoryRequestRepository {
    suspend fun getAllHistories(): List<HistoryRequest>

    suspend fun insertHistoryRequest(history: HistoryRequest)

    suspend fun updateHistoryRequest(historyItem: HistoryRequest)

    suspend fun deleteHistoryRequest(history: HistoryRequest)

    suspend fun getHistoryRequest(historyId: Int): HistoryRequest
}
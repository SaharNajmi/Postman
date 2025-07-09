package com.example.postman.domain.repository

import com.example.postman.domain.model.HistoryRequestModel

interface HistoryRequestRepository {
    suspend fun getAllHistories(): List<HistoryRequestModel>

    suspend fun insertHistoryRequest(history: HistoryRequestModel)

    suspend fun updateHistoryRequest(historyItem: HistoryRequestModel)

    suspend fun deleteHistoryRequest(history: HistoryRequestModel)

    suspend fun getHistoryRequest(historyId: Int): HistoryRequestModel
}
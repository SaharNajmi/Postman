package com.example.postman.data.repository

import com.example.postman.data.local.dao.HistoryRequestDao
import com.example.postman.data.mapper.toDomain
import com.example.postman.data.mapper.toEntity
import com.example.postman.domain.model.History
import com.example.postman.domain.repository.HistoryRepository

class HistoryRepositoryImp(private val historyRequestDao: HistoryRequestDao) :
    HistoryRepository {
    override suspend fun getAllHistories(): List<History> =
        historyRequestDao.getAllHistories().map { it.toDomain() }

    override suspend fun insertHistoryRequest(history: History) =
        historyRequestDao.insertHistoryRequest(history.toEntity())

    override suspend fun updateHistoryRequest(history: History) =
        historyRequestDao.updateHistoryRequest(history.toEntity())

    override suspend fun deleteHistoryRequest(history: History) =
        historyRequestDao.deleteHistoryRequest(history.toEntity())

    override suspend fun getHistoryRequest(historyId: Int): History =
        historyRequestDao.getHistoryRequest(historyId).toDomain()

}
package com.example.postman.data.repository

import com.example.postman.data.local.dao.HistoryRequestDao
import com.example.postman.data.local.entity.HistoryRequestEntity
import com.example.postman.data.mapper.toDomain
import com.example.postman.data.mapper.toEntity
import com.example.postman.domain.model.HistoryRequest
import com.example.postman.domain.repository.HistoryRequestRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.map

class HistoryRequestRepositoryImp(private val historyRequestDao: HistoryRequestDao) :
    HistoryRequestRepository {
    override suspend fun getAllHistories(): List<HistoryRequest> =
        historyRequestDao.getAllHistories().map { it.toDomain() }

    override suspend fun insertHistoryRequest(history: HistoryRequest) =
        historyRequestDao.insertHistoryRequest(history.toEntity())

    override suspend fun updateHistoryRequest(history: HistoryRequest) =
        historyRequestDao.updateHistoryRequest(history.toEntity())

    override suspend fun deleteHistoryRequest(history: HistoryRequest) =
        historyRequestDao.deleteHistoryRequest(history.toEntity())

    override suspend fun getHistoryRequest(historyId: Int): HistoryRequest =
        historyRequestDao.getHistoryRequest(historyId).toDomain()

}
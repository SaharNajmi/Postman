package com.example.postman.data.repository

import com.example.postman.data.local.dao.HistoryRequestDao
import com.example.postman.data.mapper.toDomain
import com.example.postman.data.mapper.toEntity
import com.example.postman.domain.model.HistoryRequestModel
import com.example.postman.domain.repository.HistoryRequestRepository

class HistoryRequestRepositoryImp(private val historyRequestDao: HistoryRequestDao,) :
    HistoryRequestRepository {
    override suspend fun getAllHistories(): List<HistoryRequestModel> =
        historyRequestDao.getAllHistories().map { it.toDomain() }

    override suspend fun insertHistoryRequest(history: HistoryRequestModel) =
        historyRequestDao.insertHistoryRequest(history.toEntity())

    override suspend fun updateHistoryRequest(history: HistoryRequestModel) =
        historyRequestDao.updateHistoryRequest(history.toEntity())

    override suspend fun deleteHistoryRequest(history: HistoryRequestModel) =
        historyRequestDao.deleteHistoryRequest(history.toEntity())

    override suspend fun getHistoryRequest(historyId: Int): HistoryRequestModel =
        historyRequestDao.getHistoryRequest(historyId).toDomain()

}
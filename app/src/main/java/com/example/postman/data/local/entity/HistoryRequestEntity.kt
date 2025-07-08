package com.example.postman.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.postman.presentation.MethodName

@Entity(tableName = "histories")
data class HistoryRequestEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val requestUrl: String,
    val methodOption: MethodName,
    val response: String
)
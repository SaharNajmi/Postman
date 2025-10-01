package com.example.postman.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.postman.common.utils.MethodName

@Suppress("ArrayInDataClass")
@Entity(tableName = "collections")
data class CollectionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val collectionId: Int,
    val collectionName: String,
    val requestName: String,
    val requestUrl: String,
    val methodOption: MethodName,
    val response: String,
    val imageResponse: ByteArray? = null,
    val createdAt: Long,
    val statusCode: Int?,
    val body: String? = null,
    val headers: List<Pair<String, String>>? = null
)
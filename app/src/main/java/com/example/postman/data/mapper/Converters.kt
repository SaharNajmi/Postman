package com.example.postman.data.mapper

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromMap(value: Map<String, String>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toMap(value: String?): Map<String, String>? {
        if (value == null) return null
        val mapType = object : TypeToken<Map<String, String>>() {}.type
        return gson.fromJson(value, mapType)
    }

    @TypeConverter
    fun fromPair(value: List<Pair<String, String>>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toPair(value: String?): List<Pair<String, String>>? {
        if (value == null) return null
        val pairType = object : TypeToken<List<Pair<String, String>>>() {}.type
        return gson.fromJson(value, pairType)
    }
}
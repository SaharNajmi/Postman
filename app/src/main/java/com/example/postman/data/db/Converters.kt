package com.example.postman.data.db

import androidx.room.TypeConverter
import com.example.postman.core.KeyValueList
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromPair(value: KeyValueList?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toPair(value: String?): KeyValueList? {
        if (value == null) return null
        val pairType = object : TypeToken<KeyValueList>() {}.type
        return gson.fromJson(value, pairType)
    }
}
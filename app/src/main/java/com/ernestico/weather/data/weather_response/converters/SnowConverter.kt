package com.ernestico.weather.data.weather_response.converters

import androidx.room.TypeConverter
import com.ernestico.weather.data.weather_response.Snow
import com.ernestico.weather.data.weather_response.Sys
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SnowConverter {
    @TypeConverter
    fun fromSnowType(value: Snow?): String = Json.encodeToString(value)

    @TypeConverter
    fun toSnowType(value: String): Snow? = Json.decodeFromString(value)
}
package com.ernestico.weather.data.weather_response.converters

import androidx.room.TypeConverter
import com.ernestico.weather.data.weather_response.Main
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MainConverter {
    @TypeConverter
    fun fromMainType(value: Main?): String = Json.encodeToString(value)

    @TypeConverter
    fun toMainType(value: String): Main? = Json.decodeFromString(value)
}
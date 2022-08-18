package com.ernestico.weather.data.weather_response.converters

import androidx.room.TypeConverter
import com.ernestico.weather.data.weather_response.Clouds
import com.ernestico.weather.data.weather_response.Wind
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class WindConverter {
    @TypeConverter
    fun fromWindType(value: Wind?): String = Json.encodeToString(value)

    @TypeConverter
    fun toWindType(value: String): Wind? = Json.decodeFromString(value)
}
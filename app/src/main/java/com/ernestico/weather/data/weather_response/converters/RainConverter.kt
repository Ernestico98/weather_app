package com.ernestico.weather.data.weather_response.converters

import androidx.room.TypeConverter
import com.ernestico.weather.data.weather_response.Rain
import com.ernestico.weather.data.weather_response.Sys
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RainConverter {
    @TypeConverter
    fun fromRainType(value: Rain?): String = Json.encodeToString(value)

    @TypeConverter
    fun toRainType(value: String): Rain? = Json.decodeFromString(value)
}
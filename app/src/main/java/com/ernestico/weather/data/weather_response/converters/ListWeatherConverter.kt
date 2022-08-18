package com.ernestico.weather.data.weather_response.converters

import androidx.room.TypeConverter
import com.ernestico.weather.data.weather_response.Clouds
import com.ernestico.weather.data.weather_response.Weather
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ListWeatherConverter {
    @TypeConverter
    fun fromListWeatherType(value: List<Weather>?): String = Json.encodeToString(value)

    @TypeConverter
    fun toListWeatherType(value: String): List<Weather>? = Json.decodeFromString(value)
}
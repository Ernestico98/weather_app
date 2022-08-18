package com.ernestico.weather.data.weather_response.converters

import androidx.room.TypeConverter
import com.ernestico.weather.data.weather_response.Clouds
import com.ernestico.weather.data.weather_response.Weather
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class WeatherConverter {
    @TypeConverter
    fun fromWeatherType(value: Weather?): String = Json.encodeToString(value)

    @TypeConverter
    fun toWeatherType(value: String): Weather? = Json.decodeFromString(value)
}
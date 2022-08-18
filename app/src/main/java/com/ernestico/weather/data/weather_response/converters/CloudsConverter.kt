package com.ernestico.weather.data.weather_response.converters

import androidx.room.TypeConverter
import com.ernestico.weather.data.weather_response.Clouds
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CloudsConverter {
    @TypeConverter
    fun fromCloudsType(value: Clouds?): String = Json.encodeToString(value)

    @TypeConverter
    fun toCloudsType(value: String): Clouds? = Json.decodeFromString(value)
}
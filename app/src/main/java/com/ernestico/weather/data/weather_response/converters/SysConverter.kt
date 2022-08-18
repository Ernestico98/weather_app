package com.ernestico.weather.data.weather_response.converters

import androidx.room.TypeConverter
import com.ernestico.weather.data.weather_response.Clouds
import com.ernestico.weather.data.weather_response.Sys
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SysConverter {
    @TypeConverter
    fun fromSysType(value: Sys?): String = Json.encodeToString(value)

    @TypeConverter
    fun toSysType(value: String): Sys? = Json.decodeFromString(value)
}
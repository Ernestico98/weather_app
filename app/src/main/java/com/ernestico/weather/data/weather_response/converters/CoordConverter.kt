package com.ernestico.weather.data.weather_response.converters

import androidx.room.TypeConverter
import com.ernestico.weather.data.weather_response.Clouds
import com.ernestico.weather.data.weather_response.Coord
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CoordConverter {
    @TypeConverter
    fun fromCoordType(value: Coord?): String = Json.encodeToString(value)

    @TypeConverter
    fun toCoordType(value: String): Coord? = Json.decodeFromString(value)
}
package com.ernestico.weather.data.weather_response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ernestico.weather.data.weather_response.converters.*
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import kotlinx.serialization.Serializable

@Entity(tableName = "WeatherData")
@Parcelize
@Serializable
data class WeatherData(
    @PrimaryKey(autoGenerate = false)
    val id: Int?,

    val base: String?,

    @field:TypeConverters(CloudsConverter::class)
    val clouds: Clouds?,
    val cod: Int?,

    @field:TypeConverters(CoordConverter::class)
    val coord: Coord?,
    val dt: Int?,

    @field:TypeConverters(MainConverter::class)
    val main: Main?,

    val name: String?,

    @field:TypeConverters(SysConverter::class)
    val sys: Sys?,
    val timezone: Int?,
    val visibility: Int?,

    @field:TypeConverters(ListWeatherConverter::class)
    val weather: List<Weather>?,

    @field:TypeConverters(WindConverter::class)
    val wind: Wind?,

    @field:TypeConverters(RainConverter::class)
    val rain: Rain?,

    @field:TypeConverters(SnowConverter::class)
    val snow: Snow?
) : Parcelable
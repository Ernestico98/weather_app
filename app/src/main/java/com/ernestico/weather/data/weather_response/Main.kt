package com.ernestico.weather.data.weather_response

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Entity(tableName = "Main")
@Parcelize
@Serializable
data class Main(
    val feels_like: Double?,
    val humidity: Int?,
    val pressure: Int?,
    val temp: Double?,
    val temp_max: Double?,
    val temp_min: Double?
) : Parcelable
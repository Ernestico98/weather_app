package com.ernestico.weather.data.weather_response

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Entity(tableName = "Weather")
@Parcelize
@Serializable
data class Weather(
    val description: String?,
    val icon: String?,
    val id: Int?,
    val main: String?
) : Parcelable
package com.ernestico.weather.data.weather_response

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Entity(tableName = "Coord")
@Parcelize
@Serializable
data class Coord(
    val lat: Double?,
    val lon: Double?
) : Parcelable
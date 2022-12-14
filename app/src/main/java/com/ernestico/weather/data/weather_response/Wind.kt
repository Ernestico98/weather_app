package com.ernestico.weather.data.weather_response

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Entity(tableName = "Wind")
@Parcelize
@Serializable
data class Wind(
    val deg: Int?,
    val speed: Double?
) : Parcelable
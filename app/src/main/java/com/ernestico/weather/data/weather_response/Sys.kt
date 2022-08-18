package com.ernestico.weather.data.weather_response

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Entity(tableName = "Sys")
@Parcelize
@Serializable
data class Sys(
    val country: String?,
    val id: Int?,
    val message: Double?,
    val sunrise: Int?,
    val sunset: Int?,
    val type: Int?
) : Parcelable
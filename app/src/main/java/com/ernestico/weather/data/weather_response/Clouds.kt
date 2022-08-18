package com.ernestico.weather.data.weather_response

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Entity(tableName = "Clouds")
@Parcelize
@Serializable
data class Clouds(
    val all: Int?
) : Parcelable
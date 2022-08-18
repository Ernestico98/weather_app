package com.ernestico.weather.data.weather_response

import android.os.Parcelable
import androidx.room.Entity
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Entity(tableName = "Rain")
@Parcelize
@Serializable
data class Rain(
    @Json(name = "1h")
    val h1: Double?,
    @Json(name = "3h")
    val h3: Double?
) : Parcelable
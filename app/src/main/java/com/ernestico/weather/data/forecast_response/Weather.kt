package com.ernestico.weather.data.forecast_response

data class Weather(
    val description: String?,
    val icon: String?,
    val id: Int?,
    val main: String?
)
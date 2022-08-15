package com.ernestico.weather.data.forecast_response

data class ForecastData(
    val city: City?,
    val cnt: Int?,
    val cod: String?,
    val list: List<Forecast>?,
    val message: Int?
)
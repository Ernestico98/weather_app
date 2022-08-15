package com.ernestico.weather.data.forecast_response

data class Forecast(
    val clouds: Clouds?,
    val dt: Int?,
    val dt_txt: String?,
    val main: Main?,
    val pop: Double?,
    val sys: Sys?,
    val visibility: Int?,
    val weather: List<Weather?>?,
    val wind: Wind?
)
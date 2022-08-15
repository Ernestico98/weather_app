package com.ernestico.weather.data.cb

import com.ernestico.weather.data.weather_response.WeatherData

interface WeatherResult {
    fun onWeatherFetchedSuccess(weather: WeatherData)

    fun onWeatherFetchedFailed()
}
package com.ernestico.weather.data.cb

import com.ernestico.weather.data.forecast_response.ForecastData

interface ForecastResult {
    fun onForecastFetchedSuccess(forecast: ForecastData)

    fun onForecastFetchedFailed()
}
package com.ernestico.weather.data

import com.ernestico.weather.data.forecast_response.ForecastData
import com.ernestico.weather.data.geo_response.GeoDataItem
import com.ernestico.weather.data.weather_response.WeatherData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

private const val ACCESS_KEY = "8bd8934268e3cd8e8c85eabbcddba5e0"

interface ForecastApiClient {
    @GET("data/2.5/forecast")
    fun fetchForecast(
        @Query(value = "APPID") appid : String = ACCESS_KEY,
        @Query(value = "lon") lon : Double,
        @Query(value = "lat") lat : Double,
        @Query(value = "units") units : String = "metric")
            : Call<ForecastData>
}
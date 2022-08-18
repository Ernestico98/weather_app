package com.ernestico.weather

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.ernestico.weather.data.forecast_response.Forecast
import com.ernestico.weather.data.forecast_response.ForecastData
import com.ernestico.weather.data.weather_response.WeatherData
import com.google.android.gms.location.FusedLocationProviderClient
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import kotlin.math.abs

class GetSortedDailyForecast {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getSortedDailyForcast(forecast : ForecastData) : SortedMap<String, MutableList<Forecast>> {
        val sortedForecast = forecast.list!!.sortedBy { item -> item.dt_txt }
        var dailyForecast = mutableMapOf<String, MutableList<Forecast>>()
        for (forecast in sortedForecast) {
            val (date, time) = forecast.dt_txt!!.split(' ')
            val timeDateUTC = ZonedDateTime.parse(date + "T" + time + "Z")
            val timeZoneConverted = timeDateUTC.withZoneSameInstant(ZoneId.systemDefault())
            val convertedDate = timeZoneConverted.toLocalDate().toString()

            if (convertedDate == LocalDate.now().toString())
                continue

            if (!(convertedDate in dailyForecast))
                dailyForecast[convertedDate] = mutableListOf<Forecast>()

            dailyForecast[convertedDate]!!.add(forecast)
        }

        val sortedDailyForecast = dailyForecast.toSortedMap()
        return sortedDailyForecast
    }
}

class GetLatestSavedData {
    private val eps = 1e-2
    public fun getFromDatabase(lon: Double, lat : Double, Items: List<WeatherData>?) : WeatherData? {
        // Get latest data for a location on database
        var weatherDataRow : WeatherData? = null
        for (row in Items!!) {
            if (abs(lon - row.coord!!.lon!!) < eps && abs(lat - row.coord!!.lat!!) < eps) {
                if (weatherDataRow == null || weatherDataRow.dt!! < row.dt!!)
                    weatherDataRow = row
            }
        }
        return weatherDataRow
    }
}

private val eps = 1e-2
fun getFromDatabase(lon: Double, lat : Double, Items: List<WeatherData>?) : WeatherData? {
    // Get latest data for a location on database
    var weatherDataRow : WeatherData? = null
    for (row in Items!!) {
        if (abs(lon - row.coord!!.lon!!) < eps && abs(lat - row.coord!!.lat!!) < eps) {
            if (weatherDataRow == null || weatherDataRow.dt!! < row.dt!!)
                weatherDataRow = row
        }
    }
    return weatherDataRow
}


@SuppressLint("MissingPermission")
fun fetchLocationAndWeather(fusedLocationProviderClient: FusedLocationProviderClient, mainViewModel: MainViewModel) {
    val task = fusedLocationProviderClient.lastLocation

    task.addOnSuccessListener {
        if (it != null) {
            Log.d("GET LOCATION SUCCESS", "${it.latitude}, ${it.longitude}")
            mainViewModel.setLocation(lon = it.longitude, lat = it.latitude)
            mainViewModel.fetchWeather(lon = it.longitude, lat = it.latitude)
            mainViewModel.fetchForecast(lon = it.longitude, lat = it.latitude)
        }
    }
}

fun checkForInternet(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    } else {
        @Suppress("DEPRECATION") val networkInfo =
            connectivityManager.activeNetworkInfo ?: return false
        @Suppress("DEPRECATION")
        return networkInfo.isConnected
    }
}
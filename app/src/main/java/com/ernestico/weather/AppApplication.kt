package com.ernestico.weather

import android.app.Application
import com.ernestico.weather.data.weather_response.WeatherData
import com.ernestico.weather.database.AppDatabase
import com.ernestico.weather.database.WeatherDataRepository


class AppApplication : Application() {
    private val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { WeatherDataRepository(database.weatherDataDao()) }
}
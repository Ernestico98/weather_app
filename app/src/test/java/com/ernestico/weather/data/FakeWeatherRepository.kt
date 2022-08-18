package com.ernestico.weather.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.ernestico.weather.data.weather_response.WeatherData
import com.ernestico.weather.database.AppDatabase
import com.ernestico.weather.database.WeatherDataDao
import com.ernestico.weather.database.WeatherDataRepository

class FakeWeatherRepository{
    val allItems = mutableListOf<WeatherData>()

    fun insertItem(item: WeatherData) {
        allItems.add(item)
    }
}


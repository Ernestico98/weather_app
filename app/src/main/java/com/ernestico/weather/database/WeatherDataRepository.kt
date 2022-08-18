package com.ernestico.weather.database

import android.util.Log
import androidx.lifecycle.LiveData
import com.ernestico.weather.data.weather_response.WeatherData

private val TAG = "REPOSITORY"

open class WeatherDataRepository(private val weatherDataDao: WeatherDataDao) {

    val allItems: LiveData<List<WeatherData>> = weatherDataDao.getItems()

    fun insertItem(item: WeatherData) {
        Log.d(TAG, "insert item | ${item}")
        AppDatabase.databaseWriteExecutor.execute {
            weatherDataDao.insertItem(item)
        }
    }

    fun insertItems(items: List<WeatherData>) {
        AppDatabase.databaseWriteExecutor.execute {
            weatherDataDao.insertItems(items)
        }
    }

    fun deleteAll() {
        AppDatabase.databaseWriteExecutor.execute {
            weatherDataDao.deleteAll()
        }
    }
}
package com.ernestico.weather.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ernestico.weather.data.weather_response.WeatherData

@Dao
interface WeatherDataDao {
    @Query("SELECT * FROM WeatherData ORDER BY dt ASC")
    fun getItems(): LiveData<List<WeatherData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: WeatherData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItems(items: List<WeatherData>)

    @Query("DELETE FROM WeatherData")
    fun deleteAll()
}
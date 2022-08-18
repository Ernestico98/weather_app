package com.ernestico.weather.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ernestico.weather.data.weather_response.WeatherData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(entities = [ WeatherData::class, ], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun weatherDataDao(): WeatherDataDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val db = Room.databaseBuilder(context, AppDatabase::class.java, "weather_db").build()
                INSTANCE = db
                db
            }
        }

        val databaseWriteExecutor: ExecutorService = Executors.newFixedThreadPool(2)
    }
}
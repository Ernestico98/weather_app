package com.ernestico.weather

import android.util.Log
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ernestico.weather.data.FakeWeatherRepository
import com.ernestico.weather.data.weather_response.*
import com.ernestico.weather.data.weather_response.converters.*
import org.junit.Before
import org.junit.Test
import kotlin.random.Random


class GetLatestSavedDataTest {
    private lateinit var repository: FakeWeatherRepository

    private val latitude = 5.3
    private val longitude = -10.2

    @Before
    fun setup() {
        repository = FakeWeatherRepository()

        (0..20).forEach { index ->
            repository.insertItem( WeatherData (
                id = index,
                base = null,
                clouds = null,
                cod = null,
                coord = Coord(lat = latitude, lon = longitude),
                dt = Random.nextInt(1000000),
                main = null,
                name = null,
                sys = null,
                timezone = null,
                visibility = null,
                weather = null,
                wind = null,
                rain = null,
                snow = null
            ))
        }
    }

    @Test
    fun `should return the latest (greatest dt value) entry from that location`() {
        val latestSavedDataReturned = GetLatestSavedData().getFromDatabase(lon = longitude, lat = latitude, Items = repository.allItems)
        val latestSavedDataReal = repository.allItems.maxBy { it.dt!! }

        assert(latestSavedDataReal.id == latestSavedDataReturned!!.id)
    }
}
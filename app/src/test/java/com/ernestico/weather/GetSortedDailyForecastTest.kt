package com.ernestico.weather

import com.ernestico.weather.data.forecast_response.City
import com.ernestico.weather.data.forecast_response.Forecast
import com.ernestico.weather.data.forecast_response.ForecastData
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

class GetSortedDailyForecastTest {

    fun addTrailingZero(num : Int) : String {
        if (num < 10)
            return "0"
        return ""
    }

    fun getRandomDate() : String {
        val year = Random.nextInt(2000, 2050)
        val month = Random.nextInt(1, 12)
        val day = Random.nextInt(1, 25)

        val date = "$year-${addTrailingZero(month)}$month-${addTrailingZero(day)}$day"
        return date
    }

    fun getRandomTime() : String {
        val hour = Random.nextInt(0, 11)
        val minute = Random.nextInt(0, 59)
        val second = Random.nextInt(0, 59)

        val time = "${addTrailingZero(hour)}$hour:${addTrailingZero(minute)}$minute:${addTrailingZero(second)}$second"
        return time
    }

    private lateinit var forecast : ForecastData

    @Before
    fun setup() {
        val forecastList = mutableListOf<Forecast>()
        for (dates in (1..10)) {
            val date = getRandomDate()
            for (times in (1..10)) {
                val time = getRandomTime()
                val dateTime = "$date $time"
                val item = Forecast(
                    clouds = null,
                    dt = null,
                    dt_txt = dateTime,
                    main = null,
                    pop = null,
                    sys = null,
                    visibility = null,
                    weather = null,
                    wind = null
                )
                forecastList.add(item)
            }
        }

        forecast = ForecastData(
            city = null,
            cnt = null,
            cod = null,
            list = forecastList,
            message = null
        )
    }

    @Test
    fun `shold return the entries per day sorted per time`() {
        val sortedDailyForecast = GetSortedDailyForecast().getSortedDailyForcast(forecast)
        for ((key, day) in sortedDailyForecast) {
            var last = "0000-00-00 00:00:00"
            for (entry in day) {
                assert(last <= entry.dt_txt!!)
                last = entry.dt_txt!!
            }
        }
    }
}
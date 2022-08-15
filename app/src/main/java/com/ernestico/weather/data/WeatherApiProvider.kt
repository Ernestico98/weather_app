package com.ernestico.weather.data

import android.util.Log
import com.ernestico.weather.data.cb.WeatherResult
import com.ernestico.weather.data.weather_response.WeatherData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.moshi.MoshiConverterFactory

private const val TAG = "WEATHER_API_PROVIDER"
private const val BASE_URL = "https://api.openweathermap.org/"

// http://openweathermap.org/img/wn/{<icon_code>}@2x.png -> to  get the icon of the weather

// http://api.openweathermap.org/geo/1.0/direct?q={city name},{state code},{country code}&limit={limit}&appid={API key} -> to get the city locations

class WeatherApiProvider {

    private val retrofit by lazy {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create<WeatherApiClient>()
    }

    fun fetchWeather(lon: Double, lat: Double, cb: WeatherResult) {
        retrofit.fetchWeather(lon = lon, lat = lat).enqueue(object : Callback<WeatherData> {
            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                if (response.isSuccessful && response.body() != null) {
                    Log.d(TAG,  "fetchWeather | response : {${response.body()!!}}")
                    cb.onWeatherFetchedSuccess(response.body()!!)
                } else {
                    Log.d(TAG, "${response.body()}")
                    cb.onWeatherFetchedFailed()
                }
            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                Log.e(TAG, "fetchWeather | error", t)
                cb.onWeatherFetchedFailed()
            }
        })
    }

}
package com.ernestico.weather.data

import android.util.Log
import com.ernestico.weather.data.cb.ForecastResult
import com.ernestico.weather.data.cb.WeatherResult
import com.ernestico.weather.data.forecast_response.ForecastData
import com.ernestico.weather.data.weather_response.WeatherData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.moshi.MoshiConverterFactory

private const val TAG = "WEATHER_API_PROVIDER"
private const val BASE_URL = "https://api.openweathermap.org/"

class ForecastApiProvider {

    private val retrofit by lazy {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create<ForecastApiClient>()
    }

    fun fetchForecast(lon: Double, lat: Double, cb: ForecastResult) {
        retrofit.fetchForecast(lon = lon, lat = lat).enqueue(object : Callback<ForecastData> {
            override fun onResponse(call: Call<ForecastData>, response: Response<ForecastData>) {
                if (response.isSuccessful && response.body() != null) {
                    Log.d(TAG,  "fetchForecast | response : {${response.body()!!}}")
                    cb.onForecastFetchedSuccess(response.body()!!)
                } else {
                    Log.d(TAG, "${response.body()}")
                    cb.onForecastFetchedFailed()
                }
            }

            override fun onFailure(call: Call<ForecastData>, t: Throwable) {
                Log.e(TAG, "fetchForecast | error", t)
                cb.onForecastFetchedFailed()
            }
        })
    }

}
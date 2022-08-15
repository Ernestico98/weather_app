package com.ernestico.weather.data

import android.util.Log
import com.ernestico.weather.data.cb.GeoResult
import com.ernestico.weather.data.geo_response.GeoDataItem
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.moshi.MoshiConverterFactory

private const val TAG = "GEO_API_PROVIDER"
private const val BASE_URL = "https://api.openweathermap.org/"

// http://openweathermap.org/img/wn/{<icon_code>}@2x.png -> to  get the icon of the weather

// http://api.openweathermap.org/geo/1.0/direct?q={city name},{state code},{country code}&limit={limit}&appid={API key} -> to get the city locations

class GeoApiProvider {

    private val retrofit by lazy {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create<GeoApiClient>()
    }

    fun fetchGeo(place: String, cb: GeoResult) {
        retrofit.fetchGeo(query = place).enqueue(object : Callback<List<GeoDataItem>> {
            override fun onResponse(call: Call<List<GeoDataItem>>, response: Response<List<GeoDataItem>>) {
                if (response.isSuccessful && response.body() != null) {
                    Log.d(TAG,  "fetchGeo | response : {${response.body()!!}}")
                    cb.onGeoFetchedSuccess(response.body()!!)
                } else {
                    Log.d(TAG, "${response.body()}")
                    cb.onGeoFetchedFailed()
                }
            }

            override fun onFailure(call: Call<List<GeoDataItem>>, t: Throwable) {
                Log.e(TAG, "fetchGeo | error", t)
                cb.onGeoFetchedFailed()
            }
        })
    }
}
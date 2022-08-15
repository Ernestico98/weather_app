package com.ernestico.weather.data

import com.ernestico.weather.data.geo_response.GeoDataItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

private const val ACCESS_KEY = "8bd8934268e3cd8e8c85eabbcddba5e0"

interface GeoApiClient {
    @GET("geo/1.0/direct")
    fun fetchGeo(
        @Query(value = "APPID") appid : String = ACCESS_KEY,
        @Query(value = "q") query : String,
        @Query(value = "limit") limit : Int = 50)
            : Call<List<GeoDataItem>>
}
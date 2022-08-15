package com.ernestico.weather.data.cb

import com.ernestico.weather.data.geo_response.GeoDataItem

//import com.ernestico.weather.data.geo_response.GeoData

interface GeoResult {
    fun onGeoFetchedSuccess(geo: List<GeoDataItem>)

    fun onGeoFetchedFailed()
}
package com.ernestico.weather.data.geo_response

data class GeoDataItem(
    val country: String?,
    val lat: Double?,
    val local_names: LocalNames?,
    val lon: Double?,
    val name: String?,
    val state: String?
)
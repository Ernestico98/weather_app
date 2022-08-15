package com.ernestico.weather

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient

@SuppressLint("MissingPermission")
fun fetchLocationAndWeather(fusedLocationProviderClient: FusedLocationProviderClient, mainViewModel: MainViewModel) {
    val task = fusedLocationProviderClient.lastLocation

    task.addOnSuccessListener {
        if (it != null) {
            Log.d("GET LOCATION SUCCESS", "${it.latitude}, ${it.longitude}")
            mainViewModel.setLocation(lon = it.longitude, lat = it.latitude)
            mainViewModel.fetchWeather(lon = it.longitude, lat = it.latitude)
        }
    }
}
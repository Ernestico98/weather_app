package com.ernestico.weather

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
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
            mainViewModel.fetchForecast(lon = it.longitude, lat = it.latitude)
        }
    }
}

fun checkForInternet(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    } else {
        @Suppress("DEPRECATION") val networkInfo =
            connectivityManager.activeNetworkInfo ?: return false
        @Suppress("DEPRECATION")
        return networkInfo.isConnected
    }
}
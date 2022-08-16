package com.ernestico.weather

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ernestico.weather.data.ForecastApiProvider
import com.ernestico.weather.data.GeoApiProvider
import com.ernestico.weather.data.WeatherApiProvider
import com.ernestico.weather.data.cb.ForecastResult
import com.ernestico.weather.data.cb.GeoResult
import com.ernestico.weather.data.cb.WeatherResult
import com.ernestico.weather.data.forecast_response.Forecast
import com.ernestico.weather.data.forecast_response.ForecastData
import com.ernestico.weather.data.geo_response.GeoDataItem
import com.ernestico.weather.data.weather_response.WeatherData
import com.ernestico.weather.navigation.BottomNavigationScreens
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.contains
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.set
import kotlin.collections.sorted
import kotlin.collections.sortedBy
import kotlin.collections.toList
import kotlin.collections.toSortedMap

private val TAG = "MAIN_VIEW_MODEL"

class MainViewModel : ViewModel(), WeatherResult, GeoResult, ForecastResult {

    private val _geoResponse = MutableLiveData<List<GeoDataItem>?>()
    val geoResponse: LiveData<List<GeoDataItem>?> = _geoResponse

    private val _weatherResponse = MutableLiveData<WeatherData?>()
    val weatherResponse: LiveData<WeatherData?> = _weatherResponse

    private val _forecastResponse = MutableLiveData<ForecastData?>()
    val forecastResponse: LiveData<ForecastData?> = _forecastResponse

    private val _latitude = MutableLiveData<Double?>()
    val latitude: LiveData<Double?> = _latitude

    private val _longitude = MutableLiveData<Double?>()
    val longitude: LiveData<Double?> = _longitude

    private val _useLocation = MutableLiveData<Boolean>()
    val useLocation: LiveData<Boolean> = _useLocation

    private val _topBarText = MutableLiveData<String>()
    val topBarText : LiveData<String> = _topBarText

    private val _selectedIndexBottomNavigation = MutableLiveData<Int>()
    val selectedIndexBottomNavigation : LiveData<Int> = _selectedIndexBottomNavigation

    private val _navigationStack = MutableLiveData<Stack<BottomNavigationScreens>> ()
    val navigationStack : LiveData<Stack<BottomNavigationScreens>> = _navigationStack

    var icons = mutableMapOf<String, Int>()

    private val _selectedLocation = MutableLiveData<String?> ()
    val selectedLocation : LiveData<String?> = _selectedLocation

    val dayOfTheWeek = mutableListOf<String>()
    val dayOfTheMonth = mutableListOf<String>()
    val month = mutableListOf<String>()
    val dayIcon = mutableListOf<String>()
    val dayTemp = mutableListOf<String>()
    val nightIcon = mutableListOf<String>()
    val nightTemp = mutableListOf<String>()

    private val firestore = Firebase.firestore

    init {
        _geoResponse.value = null
        _weatherResponse.value = null
        _forecastResponse.value = null
        _latitude.value = null
        _longitude.value = null
        _useLocation.value = true
        _topBarText.value = ""
        _selectedIndexBottomNavigation.value = 1
        _navigationStack.value = Stack<BottomNavigationScreens>()
        _selectedLocation.value = null
    }

    private val weatherProvider by lazy {
        WeatherApiProvider()
    }

    private val geoProvider by lazy {
        GeoApiProvider()
    }

    private val forecastProvider by lazy {
        ForecastApiProvider()
    }

    fun fetchWeather(lon: Double, lat: Double) {
        weatherProvider.fetchWeather(lon = lon, lat = lat, cb = this)
    }

    fun fetchGeo(place : String) {
        geoProvider.fetchGeo(place = place, cb = this)
    }

    fun fetchForecast(lon: Double, lat: Double) {
        forecastProvider.fetchForecast(lon = lon, lat = lat, cb = this)
    }

    fun setLocation(lon: Double?, lat: Double?) {
        _longitude?.value = lon
        _latitude?.value = lat
    }

    fun setTopBarText(text : String) {
        _topBarText.value = text
    }

    fun setGeoData(geo : List<GeoDataItem>? ) {
        _geoResponse.value = geo
    }

    fun setUseLocation(useLoc : Boolean) {
        _useLocation.value = useLoc
    }

    fun setBottomNavigationIndex(index : Int) {
        _selectedIndexBottomNavigation.value = index
    }

    fun setWeatherResponse(weather: WeatherData?) {
        _weatherResponse.value = weather
    }

    fun setSelectedLocation(place : String?) {
        _selectedLocation.value = place
    }

    fun setForecastResponse(forecast: ForecastData?) {
        _forecastResponse.value = forecast
    }

    override fun onWeatherFetchedSuccess(weather: WeatherData) {
        Log.d(TAG, "weather fetch $weather")

        _weatherResponse.value = weather

        val record = WeatherRecord(
            id = _weatherResponse.value!!.id!!,
            timeStamp = Timestamp.now(),
            name = _weatherResponse.value!!.name!!,
            longitude = _weatherResponse.value!!.coord!!.lon!!,
            latitude = _weatherResponse.value!!.coord!!.lat!!
        )

        sendWeatherRecord(record)
    }

    override fun onWeatherFetchedFailed() {
        Log.d(TAG, "weather fetch failed")
    }

    override fun onGeoFetchedSuccess(geo: List<GeoDataItem>) {
        Log.d(TAG, "geo fetched $geo")
        _geoResponse.value = geo
    }

    override fun onGeoFetchedFailed() {
        Log.d(TAG, "geo fetch failed")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onForecastFetchedSuccess(forecast: ForecastData) {
        Log.d(TAG, "forecast fetched $forecast")

        //Process forecast
        val sortedForecast = forecast.list!!.sortedBy { item -> item.dt_txt }
        var dailyForecast = mutableMapOf<String, MutableList<Forecast>>()
        for (forecast in sortedForecast) {
            val (date, time) = forecast.dt_txt!!.split(' ')
            val timeDateUTC = ZonedDateTime.parse(date + "T" + time + "Z")
            val timeZoneConverted = timeDateUTC.withZoneSameInstant(ZoneId.systemDefault())
            val convertedDate = timeZoneConverted.toLocalDate().toString()

            if (convertedDate == LocalDate.now().toString())
                continue

            if (!(convertedDate in dailyForecast))
                dailyForecast[convertedDate] = mutableListOf<Forecast>()

            dailyForecast[convertedDate]!!.add(forecast)
        }

        val sortedDailyForecast = dailyForecast.toSortedMap()
        val keys = sortedDailyForecast.keys.toList().sorted()
        dayOfTheWeek.clear()
        dayOfTheMonth.clear()
        month.clear()
        dayIcon.clear()
        dayTemp.clear()
        nightIcon.clear()
        nightTemp.clear()

        for (i in 0..2) {
            val key = keys[i]
            val date = LocalDate.parse(key)

            val weekDay = date.dayOfWeek.toString().subSequence(0, 3).toString()
            dayOfTheWeek.add(weekDay)

            dayOfTheMonth.add(date.dayOfMonth.toString())

            month.add(date.monthValue.toString())

            dayIcon.add(sortedDailyForecast[key]!![3].weather!![0]!!.icon!!)
            dayTemp.add(sortedDailyForecast[key]!![3].main!!.temp.toString())

            nightIcon.add(sortedDailyForecast[key]!![7].weather!![0]!!.icon!!)
            nightTemp.add(sortedDailyForecast[key]!![7].main!!.temp.toString())
        }

        _forecastResponse.value = forecast
    }

    override fun onForecastFetchedFailed() {
        Log.d(TAG, "forecast fetch failed")
    }

    private val WEATHER_COLLECTION = "WeatherApiCalls"

    // This part is to record to firestore the Calls to Weather Api
    data class WeatherRecord (
        val id : Int,
        val timeStamp : Timestamp,
        val name : String,
        val longitude : Double,
        val latitude : Double
    )

    fun sendWeatherRecord(record: WeatherRecord) {
        firestore.collection(WEATHER_COLLECTION).document()
            .set(record)
            .addOnSuccessListener { Log.d(TAG, "Succesfully sent weather record to firestore ${record}") }
            .addOnFailureListener { Log.d(TAG, "Unable to write to firestore. Error: $it") }
    }
}

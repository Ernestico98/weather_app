package com.ernestico.weather.screens

import android.graphics.Paint
import android.hardware.lights.Light
import android.util.Log
import android.widget.Space
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ernestico.weather.MainViewModel
import com.ernestico.weather.aimations.LoadingAnimation
import com.ernestico.weather.fetchLocationAndWeather
import com.ernestico.weather.navigation.BottomNavigationScreens
import com.ernestico.weather.ui.theme.DarkColors
import com.ernestico.weather.ui.theme.LightColors
import com.google.android.gms.location.FusedLocationProviderClient

@Composable
fun SearchScreen(
    mainViewModel: MainViewModel,
    navController: NavController,
    fusedLocationProviderClient: FusedLocationProviderClient
) {
    mainViewModel.setTopBarText("Search Location")

    val visible = remember { mutableStateOf(false) }
    val searchEnabled = remember { mutableStateOf(true) }
    val textValue = remember { mutableStateOf("") }
    val showList = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier
                .padding(top = 40.dp)
                .height(60.dp),
            onClick = {
                mainViewModel.setUseLocation(true)
                mainViewModel.setWeatherResponse(null)
                fetchLocationAndWeather(fusedLocationProviderClient, mainViewModel)

                mainViewModel.setBottomNavigationIndex(0)
                mainViewModel.navigationStack.value!!.push(BottomNavigationScreens.Search)
                navController.navigate(BottomNavigationScreens.Weather.route)
            }
        ) {
            Text(
                text = "Use Current Location",
                fontSize = 20.sp
            )
        }

        Button(
            modifier = Modifier
                .padding(top = 40.dp)
                .height(60.dp),
            onClick = {
                visible.value = true
                searchEnabled.value = false
            },
            enabled = searchEnabled.value
        ) {
            Text(
                text = "Search for location",
                fontSize = 20.sp
            )
        }

        AnimatedVisibility(visible = visible.value) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
            ) {
                Spacer(modifier = Modifier.weight(0.15f))

                val focusManager = LocalFocusManager.current

                TextField(
                    value = textValue.value,
                    onValueChange = { value ->
                        textValue.value = value
                    },
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    singleLine = true,
                    modifier = Modifier.weight(0.7f),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                mainViewModel.setGeoData(null)
                                mainViewModel.fetchGeo(place = textValue.value)
                                showList.value = true
                                focusManager.clearFocus()
                            }
                        ) {
                            Icon(
                                Icons.Filled.Search,
                                contentDescription = "Search Button"
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.weight(0.15f))
            }
        }

        if (showList.value)
            ShowPlacesList(mainViewModel = mainViewModel, navController = navController)
    }
}

@Composable
fun ShowPlacesList(
    mainViewModel: MainViewModel,
    navController: NavController
) {
    val geoData = mainViewModel.geoResponse.observeAsState()

    if (geoData.value == null) {
        LoadingAnimation()
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp)
        ) {
            if (geoData.value != null) {
                items(geoData.value!!) { data ->
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 40.dp),
                        onClick = {
                            mainViewModel.setUseLocation(false)
                            mainViewModel.setWeatherResponse(null)
                            mainViewModel.setSelectedLocation(data.name)
                            mainViewModel.setLocation(lon = data.lon!!, lat = data.lat!!)
                            mainViewModel.fetchWeather(lon = data.lon, lat = data.lat)

                            mainViewModel.setBottomNavigationIndex(0)
                            mainViewModel.navigationStack.value!!.push(BottomNavigationScreens.Search)
                            navController.navigate(BottomNavigationScreens.Weather.route)
                        }
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                                .padding(5.dp)
                        ) {
                            Text(text = "Place name: ${data.name}")
                            Text(text = "Country code: ${data.country}")
                            Text(text = "Latitude: ${data.lat}")
                            Text(text = "Longitude: ${data.lon}")
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
        }
    }
}


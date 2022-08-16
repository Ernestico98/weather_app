package com.ernestico.weather.screens

import android.app.Activity
import android.util.Log
import android.widget.Space
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ernestico.weather.MainViewModel
import com.ernestico.weather.R
import com.ernestico.weather.aimations.LoadingAnimation
import com.ernestico.weather.data.AppPreferences
import com.google.android.gms.location.FusedLocationProviderClient

@Composable
fun WeatherScreen(
    mainViewModel: MainViewModel,
    darkMode : MutableState<Boolean>
) {
    mainViewModel.setTopBarText("Weather Status")

    val weather = mainViewModel.weatherResponse.observeAsState()
    val forecast = mainViewModel.forecastResponse.observeAsState()

    if (weather.value != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "${weather.value!!.weather!![0].main}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Image(
                painter = painterResource(id = mainViewModel.icons[weather.value!!.weather!![0].icon]!!),
                contentDescription = "Weather Icon",
                modifier = Modifier
                    .size(200.dp)
                    .scale(1.6f),
                contentScale = ContentScale.Crop,
            )

//            Spacer(modifier = Modifier.height(5.dp))
            
            Text(
                text = "Weather at ${if (mainViewModel.useLocation.value == true) "User's Location" else mainViewModel.selectedLocation.value}",
                fontSize = 18.sp,
            )

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                elevation = 10.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.temp),
                            contentDescription = "Thermometer icon",
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = "Temp.: ${weather.value!!.main!!.temp} \u2103")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row () {
                        Text(text = "Min.: ${weather.value!!.main!!.temp_min} \u2103")
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(text = "Max.: ${weather.value!!.main!!.temp_max} \u2103")
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row() {
                        Text(text = "Pressure: ${weather.value!!.main!!.pressure} hPa")
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(text = "Humidity: ${weather.value!!.main!!.humidity}%")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row() {
                        Text(text = "Wind Speed: ${weather.value!!.wind!!.speed} m/s")
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(text = "Clouds: ${weather.value!!.clouds!!.all}%")
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (forecast.value != null) {
                    ShowForecastData(mainViewModel)
                } else {
                    LoadingAnimation(darkMode = darkMode)
                }
            }
        }
    } else {
        LoadingAnimation(darkMode = darkMode)
    }
}


@Composable
fun ShowForecastData(mainViewModel: MainViewModel) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        elevation = 10.dp
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp, start = 10.dp, end = 10.dp)
            ) {
                Spacer(modifier = Modifier.weight(0.15f))
                Row(
                    modifier = Modifier.weight(0.85f),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (i in 0..2) {
                        Text(text = "${mainViewModel.dayOfTheWeek[i]} - ${mainViewModel.dayOfTheMonth[i]}")
                    }
                }
            }

            Row(modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)) {
                Column(modifier = Modifier
                    .weight(0.15f)
                    .height(180.dp)
                    .fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Image(painter = painterResource(id = R.drawable.ic_sun), contentDescription = "Day icon")
                    Image(painter = painterResource(id = R.drawable.ic_moon), contentDescription = "Night icon")
                }

                Column(modifier = Modifier
                    .fillMaxSize()
                    .weight(0.85f),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (i in 0..2) {
                            Card(
                                elevation = 10.dp
                            ) {
                                Column(modifier = Modifier.padding(6.dp),) {
                                    Image(
                                        painter = painterResource(id = mainViewModel.icons[mainViewModel.dayIcon[i]]!!),
                                        contentDescription = "Weather Icon",
                                        modifier = Modifier.size(60.dp)
                                    )

                                    Text(text = "${mainViewModel.dayTemp[i]} ℃")

                                    Image(
                                        painter = painterResource(id = mainViewModel.icons[mainViewModel.nightIcon[i]]!!),
                                        contentDescription = "Weather Icon",
                                        modifier = Modifier.size(60.dp)
                                    )

                                    Text(text = "${mainViewModel.nightTemp[i]} ℃")

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
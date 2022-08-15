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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.google.android.gms.location.FusedLocationProviderClient

@Composable
fun WeatherScreen(
    mainViewModel: MainViewModel,
) {
    mainViewModel.setTopBarText("Weather Status")

    val weather = mainViewModel.weatherResponse.observeAsState()

    if (weather.value != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "${weather.value!!.weather!![0].main}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Image(
                painter = painterResource(id = mainViewModel.icons[weather.value!!.weather!![0].icon]!!),
                contentDescription = "Weather Icon",
                modifier = Modifier
                    .size(300.dp)
                    .scale(1.6f),
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.height(15.dp))
            
            Text(
                text = "Weather at ${if (mainViewModel.useLocation.value == true) "User's Location" else mainViewModel.selectedLocation.value}",
                modifier = Modifier.padding(bottom = 5.dp),
                fontSize = 20.sp,
            )

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                elevation = 10.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
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

                    Spacer(modifier = Modifier.height(20.dp))

                    Row () {
                        Text(text = "Min.: ${weather.value!!.main!!.temp_min} \u2103")
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(text = "Max.: ${weather.value!!.main!!.temp_max} \u2103")
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    Row() {
                        Text(text = "Pressure: ${weather.value!!.main!!.pressure} hPa")
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(text = "Humidity: ${weather.value!!.main!!.humidity}%")
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row() {
                        Text(text = "Wind Speed: ${weather.value!!.wind!!.speed} m/s")
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(text = "Clouds: ${weather.value!!.clouds!!.all}%")
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row() {
                        Text(text = "Visibility: ${weather.value!!.visibility} m")
                    }



                }
            }
        }
    } else {
        LoadingAnimation()
    }
}

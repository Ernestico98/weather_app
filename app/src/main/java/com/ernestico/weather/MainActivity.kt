package com.ernestico.weather

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ernestico.weather.data.AppPreferences
import com.ernestico.weather.navigation.BottomNavigationScreens
import com.ernestico.weather.screens.AboutScreen
import com.ernestico.weather.screens.SearchScreen
import com.ernestico.weather.screens.WeatherScreen
import com.ernestico.weather.ui.theme.DarkColors
import com.ernestico.weather.ui.theme.LightColors
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

private const val TAG = "MAIN_ACTIVITY"

class MainActivity : ComponentActivity() {

    private val mainViewModel : MainViewModel by viewModels()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        createIconMap()
        val preferences = AppPreferences(this)

        setContent {

            val topBarText = mainViewModel.topBarText.observeAsState()
            val systemDarkMode = isSystemInDarkTheme()
            var darkMode = remember { mutableStateOf(preferences.getDarkTheme(systemDarkMode)) }

            MaterialTheme(
                colors = if(darkMode.value) DarkColors else LightColors
            ) {
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        TopAppBar(
                        ) {
                            Text(
                                text = "${topBarText.value}",
                                fontSize = 22.sp,
                                modifier = Modifier.padding(10.dp)
                            )
                            
                            Spacer(modifier = Modifier.weight(1f))

                            IconButton(
                                onClick = {
                                    darkMode.value = !darkMode.value
                                    preferences.setDarkTheme(darkMode.value)
                                },
                                modifier = Modifier.padding(end=10.dp)
                                    .size(40.dp)
                            ) {
                                if (darkMode.value) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.light_mode),
                                        contentDescription = "Theme mode icon"
                                    )
                                } else {
                                    Icon(
                                        painter = painterResource(id = R.drawable.dark_mode),
                                        contentDescription = "Theme mode icon"
                                    )
                                }

                            }

//                            Switch(
//                                checked = darkMode.value,
//                                onCheckedChange =  {
//                                    darkMode.value = !darkMode.value
//                                    preferences.setDarkTheme(darkMode.value)
//                                }
//                            )
                        }

                        Scaffold(
                            modifier = Modifier.padding(bottom=10.dp),
                            bottomBar = {
                                    AddBottomBarNavigation(navController)
                            },
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(bottom = it.calculateBottomPadding())
                            ) {
                                NavHost(
                                    navController = navController,
                                    startDestination = BottomNavigationScreens.Search.route
                                ) {
                                    composable(BottomNavigationScreens.Weather.route) {
                                        WeatherScreen(
                                            mainViewModel = mainViewModel,
                                        )
                                    }
                                    composable(BottomNavigationScreens.Search.route) {
                                        SearchScreen(
                                            mainViewModel = mainViewModel,
                                            navController = navController,
                                            fusedLocationProviderClient = fusedLocationProviderClient
                                        )
                                    }
                                    composable(BottomNavigationScreens.About.route) {
                                        AboutScreen(
                                            mainViewModel = mainViewModel,
                                            navController = navController
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
        }
    }

    override fun onBackPressed() {
        if (!mainViewModel.navigationStack.value!!.empty()) {
            val top = mainViewModel.navigationStack.value!!.pop()
            when(top.route) {
                "Weather" -> mainViewModel.setBottomNavigationIndex(0)
                "Search" -> mainViewModel.setBottomNavigationIndex(1)
                "About" -> mainViewModel.setBottomNavigationIndex(2)
            }
        }
        super.onBackPressed()
    }

    @Composable
    fun AddBottomBarNavigation(
        navController: NavHostController,
    ) {
        val items = listOf(
            BottomNavigationScreens.Weather,
            BottomNavigationScreens.Search,
            BottomNavigationScreens.About
        )

        val selectedIndex = mainViewModel.selectedIndexBottomNavigation.observeAsState()

        BottomNavigation(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .clip(RoundedCornerShape(30.dp))
        ) {
            items.forEachIndexed { index, bottomNavigationScreen ->

                val isSelected = (index == selectedIndex.value)
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = bottomNavigationScreen.drawRess),
                            contentDescription = stringResource(id = bottomNavigationScreen.stringResId),
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {   
                        Text(text = stringResource(id = bottomNavigationScreen.stringResId))
                    },
                    selected = isSelected,
                    alwaysShowLabel = true,
                    onClick = {
                        if (!isSelected) {
                            mainViewModel.navigationStack.value!!.push(items[mainViewModel.selectedIndexBottomNavigation.value!!])
                            mainViewModel.setBottomNavigationIndex(index)

                            if (index == 0 && mainViewModel.useLocation.value == true)
                                fetchLocationAndWeather(fusedLocationProviderClient, mainViewModel)

                            navController.navigate(bottomNavigationScreen.route)
                        }
                    }
                )
            }
        }
    }

    private fun createIconMap() {
        mainViewModel.icons = mapOf<String, Int>(
            "01d" to R.drawable.i01d,
            "01n" to R.drawable.i01n,
            "02d" to R.drawable.i02d,
            "02n" to R.drawable.i02n,
            "03d" to R.drawable.i03d,
            "03n" to R.drawable.i03n,
            "04d" to R.drawable.i04d,
            "04n" to R.drawable.i04n,
            "09d" to R.drawable.i09d,
            "09n" to R.drawable.i09n,
            "10d" to R.drawable.i10d,
            "10n" to R.drawable.i10n,
            "11d" to R.drawable.i11d,
            "11n" to R.drawable.i11n,
            "13d" to R.drawable.i13d,
            "13n" to R.drawable.i13n,
            "50d" to R.drawable.i50d,
            "50n" to R.drawable.i50n,
        ) as MutableMap<String, Int>
    }
}

package com.ernestico.weather.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ernestico.weather.R

sealed class BottomNavigationScreens(
    val route: String,
    @StringRes val stringResId: Int,
    @DrawableRes val drawRess: Int
) {

    object Weather: BottomNavigationScreens(
        route = "Weather",
        R.string.main_navigation_weather,
        R.drawable.ic_weather
    )

    object Search: BottomNavigationScreens(
        route = "Search",
        R.string.main_navigation_search,
        R.drawable.ic_search
    )

    object About: BottomNavigationScreens(
        route = "About",
        R.string.main_navigation_about,
        R.drawable.ic_info
    )
}
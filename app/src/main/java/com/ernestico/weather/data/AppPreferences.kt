package com.ernestico.weather.data

import android.app.Activity
import android.content.Context

private const val DARK_THEME_KEY = "darkTheme"

class AppPreferences(context : Activity) {

    private val preferences = context.getPreferences(Context.MODE_PRIVATE)

    fun setDarkTheme(value: Boolean) {
        with (preferences.edit()) {
            putBoolean(DARK_THEME_KEY, value)
            apply()
        }
    }

    fun getDarkTheme(onSystem: Boolean): Boolean {
        return preferences.getBoolean(DARK_THEME_KEY, onSystem)
    }
}
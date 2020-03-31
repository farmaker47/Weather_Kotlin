package com.george.weather_kotlin.weather_fragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.george.weather_kotlin.CoordinatesAndAddress

class WeatherViewModel(coordinatesAndAddress: CoordinatesAndAddress, application: Application) : AndroidViewModel(application) {


    init {
        Log.e("WeatherViewModel", coordinatesAndAddress.latitude + " " +
                coordinatesAndAddress.longtitude + " " + coordinatesAndAddress.address)
    }
}
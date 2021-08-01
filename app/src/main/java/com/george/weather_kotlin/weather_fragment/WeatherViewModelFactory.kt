package com.george.weather_kotlin.weather_fragment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.george.weather_kotlin.CoordinatesAndAddress

class WeatherViewModelFactory /*(
    private val coordinatesAndAddress: CoordinatesAndAddress,
    private val application: Application): ViewModelProvider.Factory{

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(coordinatesAndAddress, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}*/
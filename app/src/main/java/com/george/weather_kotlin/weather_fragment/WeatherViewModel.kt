package com.george.weather_kotlin.weather_fragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.george.weather_kotlin.CoordinatesAndAddress
import com.george.weather_kotlin.network.WeatherApi
import com.george.weather_kotlin.network.WeatherJsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

const val connectionAble = "ec715631be02bd4b13f25d478d34ad8e"
const val units = "metric"

class WeatherViewModel(val coordinatesAndAddress: CoordinatesAndAddress, application: Application) :
    AndroidViewModel(application) {

    // Internally, we use a MutableLiveData, because we will be updating the List of WeatherJsonObject
    // with new values
    private val _properties = MutableLiveData<WeatherJsonObject>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val properties: LiveData<WeatherJsonObject>
        get() = _properties

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        Log.i(
            "WeatherViewModel", coordinatesAndAddress.latitude + " " +
                    coordinatesAndAddress.longtitude + " " + coordinatesAndAddress.address
        )

        //get details
        getWeatherDetails()
    }

    private fun getWeatherDetails() {
        coroutineScope.launch {
            // Get the Deferred object for our Retrofit request
            val getPropertiesDeferred = WeatherApi.retrofitService.getProperties(
                coordinatesAndAddress.latitude,
                coordinatesAndAddress.longtitude,
                units,
                connectionAble
            )
            try {
                // this will run on a thread managed by Retrofit
                val listResult = getPropertiesDeferred.await()

                //properties
                _properties.value = listResult
                Log.e("PROPERTIES", _properties.toString())
            } catch (e: Exception) {
                //properties
                //_properties.value = WeatherJsonObject()
                Log.e("EXCEPTION", e.toString())
            }
        }

    }

    /**
     * When the [ViewModel] is finished, we cancel our coroutine [viewModelJob], which tells the
     * Retrofit service to stop.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
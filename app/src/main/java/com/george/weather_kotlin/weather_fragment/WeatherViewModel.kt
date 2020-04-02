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
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

const val connectionAble = "ec715631be02bd4b13f25d478d34ad8e"
const val units = "metric"

enum class WeatherApiStatus { LOADING, ERROR, DONE }

class WeatherViewModel(val coordinatesAndAddress: CoordinatesAndAddress, application: Application) :
    AndroidViewModel(application) {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<WeatherApiStatus>()
    // The external immutable LiveData for the request status
    val status: LiveData<WeatherApiStatus>
        get() = _status

    // Internally, we use a MutableLiveData, because we will be updating the List of WeatherJsonObject
    // with new values
    private val _weatherData = MutableLiveData<WeatherJsonObject>()
    // The external LiveData interface to the property is immutable, so only this class can modify
    val weatherData: LiveData<WeatherJsonObject>
        get() = _weatherData

    // Internally, we use a MutableLiveData, because we will be updating the List of WeatherJsonObject
    // with new values
    private val _dateHour = String()
    // The external LiveData interface to the property is immutable, so only this class can modify
    val dateHour: String
        get() = _dateHour

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

    fun getWeatherDetails() {
        coroutineScope.launch {
            // Get the Deferred object for our Retrofit request
            val getPropertiesDeferred = WeatherApi.retrofitService.getProperties(
                coordinatesAndAddress.latitude,
                coordinatesAndAddress.longtitude,
                units,
                connectionAble
            )
            try {
                _status.value = WeatherApiStatus.LOADING
                // this will run on a thread managed by Retrofit
                val jsonResult = getPropertiesDeferred.await()
                _status.value = WeatherApiStatus.DONE

                //properties
                _weatherData.value = jsonResult
                Log.e("RESULTS", _weatherData.toString())
            } catch (e: Exception) {
                //properties
                //_properties.value = WeatherJsonObject()
                _status.value = WeatherApiStatus.ERROR
                Log.i("EXCEPTION", e.toString())
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
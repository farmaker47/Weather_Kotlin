package com.george.weather_kotlin.weather_fragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.george.weather_kotlin.CoordinatesAndAddress
import com.george.weather_kotlin.di.NetworkRepository
import com.george.weather_kotlin.network.WeatherApiService
import com.george.weather_kotlin.network.WeatherJsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

const val connectionAble = "ec715631be02bd4b13f25d478d34ad8e"
const val units = "metric"

enum class WeatherApiStatus { LOADING, ERROR, DONE }

@HiltViewModel
class WeatherViewModel @Inject constructor(
    application: Application,
    private val networkRepository: NetworkRepository
) :
    AndroidViewModel(application) {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<WeatherApiStatus>()

    // The external immutable LiveData for the request status
    val status: LiveData<WeatherApiStatus> = _status

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

    private lateinit var coordinatesAndAddress: CoordinatesAndAddress


    init {
        /*Log.i(
            "WeatherViewModel", coordinatesAndAddress.latitude + " " +
                    coordinatesAndAddress.longtitude + " " + coordinatesAndAddress.address
        )*/

        coordinatesAndAddress = CoordinatesAndAddress("37.0366386", "22.1143716", "Kalamata")

        //get details
        //getWeatherDetails()
    }

    fun getWeatherDetails() {
        viewModelScope.launch(Dispatchers.Default) {
            // Get the Deferred object for our Retrofit request
            val getPropertiesDeferred = networkRepository.getProperties(
                coordinatesAndAddress.latitude,
                coordinatesAndAddress.longtitude,
                units,
                connectionAble
            )
            try {
                _status.value = WeatherApiStatus.LOADING
                // this will run on a thread managed by Retrofit
                val jsonResult = getPropertiesDeferred
                _status.value = WeatherApiStatus.DONE

                //properties
                _weatherData.value = jsonResult.body()
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
    }

    fun setCoordinates(coordinatesAndAddressPassed: CoordinatesAndAddress) {

        coordinatesAndAddress = coordinatesAndAddressPassed
    }

}
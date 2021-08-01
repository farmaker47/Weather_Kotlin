package com.george.news.weather_fragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.george.news.di.NetworkRepository
import com.george.news.getAPIKey
import com.george.news.network.NewsApiStatus
import com.george.news.network.NewsResponse
import com.george.news.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    application: Application,
    private val networkRepository: NetworkRepository
) :
    AndroidViewModel(application) {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<NewsApiStatus>()
    // The external immutable LiveData for the request status
    val status: LiveData<NewsApiStatus> = _status

    // Internally, we use a MutableLiveData, because we will be updating the List of WeatherJsonObject
    // with new values
    private val _weatherData = MutableLiveData<Resource<NewsResponse>>()
    // The external LiveData interface to the property is immutable, so only this class can modify
    val weatherData: LiveData<Resource<NewsResponse>> = _weatherData

    init {
        //getWeatherDetails()
        getNews()
    }

    fun getWeatherDetails() {
        viewModelScope.launch(Dispatchers.Default) {
            // Get the Deferred object for our Retrofit request



            /*Log.e("RESULT", getAPIKey())
            val result = networkRepository.getNews("us", "business", "f71af7261c434b5d8be60816ed910d8b")
            //Log.e("RESULT", result.body().toString())
            _weatherData.postValue(result.body())*/



            /*try {
                _status.value = WeatherApiStatus.LOADING
                // this will run on a thread managed by Retrofit
                _status.value = WeatherApiStatus.DONE

                //properties
                _weatherData.value = result.body()
                Log.e("RESULTS", _weatherData.toString())
            } catch (e: Exception) {
                //properties
                //_properties.value = WeatherJsonObject()
                _status.value = WeatherApiStatus.ERROR
                Log.i("EXCEPTION", e.toString())
            }*/
        }

    }

    private fun getNews()  = viewModelScope.launch(Dispatchers.Default) {
        _weatherData.postValue(Resource.loading(null))
        networkRepository.getNews("us", "business", getAPIKey()).let {
            if (it.isSuccessful){
                _weatherData.postValue(Resource.success(it.body()))
            }else{
                _weatherData.postValue(Resource.error(it.errorBody().toString(), null))
            }
        }
    }

}
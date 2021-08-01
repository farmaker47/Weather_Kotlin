package com.george.weather_kotlin

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Toolbar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.george.weather_kotlin.weather_fragment.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WeatherActivity : AppCompatActivity() {

    private var latitude: String? = null
    private var longtitude: String? = null
    private var address: String? = null
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        //val toolbar = findViewById<Toolbar>(R.id.toolbar)
        //setSupportActionBar(toolbar)
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Checking info of intent passed from MapsActivity
        val intent = intent
        if (intent.hasExtra(LATITUDE_FROM_MAPS)) {
            latitude = intent.getStringExtra(LATITUDE_FROM_MAPS)
        }
        if (intent.hasExtra(LONGTITUDE_FROM_MAPS)) {
            longtitude = intent.getStringExtra(LONGTITUDE_FROM_MAPS)
        }
        if (intent.hasExtra(ADDRESS_TO_PASS)) {
            address = intent.getStringExtra(ADDRESS_TO_PASS)
        }

        /*val bundle = Bundle()
        val toPass = CoordinatesAndAddress(latitude, longtitude, address)
        bundle.putParcelable("coordinates", toPass)*/
        weatherViewModel.setCoordinates(CoordinatesAndAddress(latitude, longtitude, address))
        //Delete navGraph from xml and set here
        // Details: https://developer.android.com/guide/navigation/navigation-pass-data#start
        findNavController(R.id.nav_host_fragment).setGraph(R.navigation.nav_graph)

        Log.i("ALL_Info", latitude + longtitude + address)
    }

}


data class CoordinatesAndAddress(
    val latitude: String?,
    val longtitude: String?,
    val address: String?
)

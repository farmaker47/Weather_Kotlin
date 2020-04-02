package com.george.weather_kotlin

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_weather.*

class WeatherActivity : AppCompatActivity() {

    private var latitude: String? = null
    private var longtitude: String? = null
    private var address: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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

        val bundle = Bundle()
        val toPass = CoordinatesAndAddress(latitude,longtitude,address)
        bundle.putParcelable("coordinates",toPass)
        //Delete navGraph from xml and set here
        findNavController(R.id.nav_host_fragment).setGraph(R.navigation.nav_graph, bundle)

        Log.e("ALL", latitude + longtitude + address)
    }

}

@Parcelize
data class CoordinatesAndAddress(
    val latitude: String?,
    val longtitude: String?,
    val address: String?) : Parcelable

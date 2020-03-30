package com.george.weather_kotlin

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_weather.*

class WeatherActivity : AppCompatActivity() {

    private var latitude: String? = null
    private var longtitude: String? = null
    private var address : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        setSupportActionBar(toolbar)

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

        Log.e("ALL",latitude+longtitude+address)

        fab.setOnClickListener { view ->
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }
    }

}

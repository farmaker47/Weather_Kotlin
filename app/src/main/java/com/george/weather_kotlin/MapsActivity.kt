package com.george.weather_kotlin

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.george.weather_kotlin.databinding.ActivityMapsBinding
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val PROGRESSBAR_STATE = "progressbar_state"
    private val REQUEST_LOCATION_PERMISSION = 111
    private var googleApiClient: GoogleApiClient? = null
    private val REQUEST_LOCATION = 199
    private var latitudeToPass: String? = null
    private var longtitudeToPass: String? = null
    private var addressToPass: String? = null
    var locationManager: LocationManager? = null
    var marker: Marker? = null
    var locationListener: LocationListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        binding.buttonMaps.setOnClickListener {

            //Check if there is internet connection
            val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connMgr.activeNetworkInfo
            // If there is a network connection ask for location
            if (networkInfo != null && networkInfo.isConnected) {
                showDialogForAutomaticLocation()
                //set progress bar to invisible
                binding.progressBarMaps.visibility = View.INVISIBLE
            } else {
                Toast.makeText(
                    this@MapsActivity,
                    getString(R.string.connectToInternet),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        if (savedInstanceState != null) {
            when (savedInstanceState.getInt(PROGRESSBAR_STATE)) {
                1 -> binding.progressBarMaps.visibility = View.VISIBLE
                2 -> binding.progressBarMaps.visibility = View.INVISIBLE
            }
        }

        //stop reloading map at rotation
        mapFragment.retainInstance = true

    }

    //Method to ask the user if he wants to find his location automatically or manually
    private fun showDialogForAutomaticLocation() {
        val downloadDialog =
            AlertDialog.Builder(this@MapsActivity)
        downloadDialog.setTitle(R.string.automaticLocation)
        downloadDialog.setMessage(R.string.automaticLocationMessage)
        downloadDialog.setPositiveButton(
            R.string.epilogiYes
        ) { dialogInterface, i -> //Check for permissions
            marshmallowGPSPremissionCheck()
        }
        downloadDialog.setNegativeButton(
            R.string.epilogiNo
        ) { dialogInterface, i -> //Do some actions if user wants to manually insert addres
            //manualInputOfAddress()
        }
        downloadDialog.show()
    }

    private fun marshmallowGPSPremissionCheck() {
        //from Marshmallow we have to check for permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                REQUEST_LOCATION_PERMISSION
            )
        } else {
            //below M we prompt to enable GPS
            promptToEnableGps()
        }
    }

    private fun promptToEnableGps() {
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Start action to find location
            locationManagerInit()
            //Set Progressbar visible
            binding.progressBarMaps.visibility = View.VISIBLE
        }
        if (!hasGPSDevice(this)) {
            Toast.makeText(this, getString(R.string.gpsNotSupported), Toast.LENGTH_SHORT).show()
        }
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(this)) {
            //Do some action to enable GPS
            enableLoc()
        }
    }

    //Method to check if there is GPS
    private fun hasGPSDevice(context: Context): Boolean {
        val mgr = context
            .getSystemService(Context.LOCATION_SERVICE) as LocationManager
            ?: return false
        val providers = mgr.allProviders ?: return false
        return providers.contains(LocationManager.GPS_PROVIDER)
    }

    //method to enable GPS and start activity for result
    private fun enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(object : ConnectionCallbacks {
                    override fun onConnected(bundle: Bundle?) {}
                    override fun onConnectionSuspended(i: Int) {
                        googleApiClient?.connect()
                    }
                })
                .addOnConnectionFailedListener { connectionResult ->
                    Log.d(
                        "Location error",
                        "Location error " + connectionResult.errorCode
                    )
                }.build()
            googleApiClient?.connect()
            val locationRequest: LocationRequest = LocationRequest.create()
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            locationRequest.setInterval(30 * 1000)
            locationRequest.setFastestInterval(5 * 1000)
            val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
            builder.setAlwaysShow(true)
            val result: PendingResult<LocationSettingsResult> =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
            result.setResultCallback { result ->
                val status: Status = result.getStatus()
                when (status.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult(
                            this@MapsActivity,
                            REQUEST_LOCATION
                        )
                    } catch (e: SendIntentException) {
                        // Ignore the error.
                    }
                }
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        @Nullable data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_LOCATION -> {
                //When GPS is enabled do some action to find location
                locationManagerInit()
                //Set Progressbar visible
                binding.progressBarMaps.visibility = View.VISIBLE
            }
        }
    }

    private fun locationManagerInit() {

        //create listener for changes in location
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val latitude = location.latitude
                val longitude = location.longitude
                //get the location name from latitude and longitude
                latitudeToPass = latitude.toString()
                longtitudeToPass = longitude.toString()
                val geocoder = Geocoder(applicationContext)
                try {
                    val addresses =
                        geocoder.getFromLocation(latitude, longitude, 1)
                    if (addresses != null && addresses.size > 0) {
                        var result = addresses[0].locality + ":"
                        result += addresses[0].adminArea
                        addressToPass = result
                        val latLng = LatLng(latitude, longitude)
                        if (marker != null) {
                            marker?.remove()
                            marker = mMap.addMarker(MarkerOptions().position(latLng).title(result))
                            mMap.setMaxZoomPreference(20f)
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f))

                            //Remove updates so you can click the marker
                            locationManager?.removeUpdates(locationListener)
                            //Set listener to marker
                            clickMarker()
                            //Set Progressbar invisible
                            binding.progressBarMaps.setVisibility(View.INVISIBLE)
                            //Show Snackbar to info to user that he/she has to click on its balloon
                            Toast.makeText(this@MapsActivity, R.string.editTextMessageInfo, Toast.LENGTH_LONG)
                                .show()
                        } else {
                            marker = mMap.addMarker(MarkerOptions().position(latLng).title(result))
                            mMap.setMaxZoomPreference(20f)
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 21.0f))
                            //Remove updates so you can click the marker
                            locationManager?.removeUpdates(locationListener)
                            //Set listener to marker
                            clickMarker()
                            //Set Progressbar invisible
                            binding.progressBarMaps.setVisibility(View.INVISIBLE)
                            //Show Snackbar to info to user that he/she has to click on its balloon
                            Toast.makeText(this@MapsActivity, R.string.editTextMessageInfo, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun onStatusChanged(
                provider: String,
                status: Int,
                extras: Bundle
            ) {
            }

            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        //enable updates
        if (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            locationManager?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0f,
                locationListener
            )
        } else if (locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0,
                0f,
                locationListener
            )
        }
    }

    private fun clickMarker() {
        //Add marker click listener
        /*mMap.setOnInfoWindowClickListener {
            val intent = Intent(this@MapsActivity, WeatherActivity::class.java)
            intent.putExtra(MapsActivity.LATITUDE_FROM_MAPS, latitudeToPass)
            intent.putExtra(MapsActivity.LONGTITUDE_FROM_MAPS, longtitudeToPass)
            intent.putExtra(MapsActivity.ADDRESS_TO_PASS, addressToPass)
            Log.d("Values", "$latitudeToPass $longtitudeToPass")
            startActivity(intent)
        }*/
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val kalamata = LatLng(37.0502925, 22.1307059)
        mMap.addMarker(MarkerOptions().position(kalamata).title(getString(R.string.kalamataMarker)))
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(kalamata))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kalamata, 8.0f))
    }
}

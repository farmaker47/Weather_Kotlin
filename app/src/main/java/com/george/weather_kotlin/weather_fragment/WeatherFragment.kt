package com.george.weather_kotlin.weather_fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.george.weather_kotlin.R

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class WeatherFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val toGet = WeatherFragmentArgs.fromBundle(arguments!!).coordinates

        Log.e("Weather_Fragment", toGet.latitude + " " + toGet.longtitude + " " + toGet.address)


        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            //findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }
}

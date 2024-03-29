package com.george.weather_kotlin.weather_fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.george.weather_kotlin.R
import com.george.weather_kotlin.databinding.WeatherFragmentBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class WeatherFragment : Fragment() {

    private lateinit var binding: WeatherFragmentBinding
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var mWeatherRecyclerViewAdapter: WeatherRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = WeatherFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this


        val application = requireNotNull(activity).application
        val toGet = WeatherFragmentArgs.fromBundle(requireArguments()).coordinates
        //Log.i("Weather_Fragment", toGet.latitude + " " + toGet.longtitude + " " + toGet.address)
        val viewModelFactory = WeatherViewModelFactory(toGet, application)
        weatherViewModel = ViewModelProvider(this, viewModelFactory).get(WeatherViewModel::class.java)

        binding.viewModel = weatherViewModel

        //Passing listener and viewmodel to adapter
        //binding.weatherRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.weatherRecyclerView.adapter =
            WeatherRecyclerViewAdapter(WeatherRecyclerViewAdapter.OnClickListener {
                //viewModel.displayPropertyDetails(it)

            }, weatherViewModel)

        binding.fab.setOnClickListener{
            weatherViewModel.getWeatherDetails()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //view.findViewById<Button>(R.id.button_first).setOnClickListener {
        //findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        //}
    }
}

package com.george.news.weather_fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.george.news.databinding.NewsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class NewsFragment : Fragment() {

    private lateinit var binding: NewsFragmentBinding
    private val newsViewModel: NewsViewModel by viewModels()
    private lateinit var mNewsRecyclerViewAdapter: NewsRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = NewsFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = newsViewModel

        //Passing listener and viewmodel to adapter
        //binding.weatherRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.weatherRecyclerView.adapter =
            NewsRecyclerViewAdapter(NewsRecyclerViewAdapter.OnClickListener {
                //viewModel.displayPropertyDetails(it)

            }, newsViewModel)

        observeViewModel()

        return binding.root
    }

    private fun observeViewModel() {
        newsViewModel.weatherData.observe(requireActivity(), {
            it.data?.articles?.get(0)?.let { it1 -> Log.v("RESULT", it1.description) }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //view.findViewById<Button>(R.id.button_first).setOnClickListener {
        //findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        //}
    }
}

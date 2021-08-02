package com.george.news.news_fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.george.news.R
import com.george.news.databinding.NewsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class NewsFragment : Fragment() {

    private lateinit var binding: NewsFragmentBinding
    private val newsViewModel: NewsViewModel by viewModels()

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

                findNavController().navigate(
                    NewsFragmentDirections.actionNewsFragmentToDetailsFragment2(
                        it.title,
                        it.content,
                        it.urlToImage
                    )
                )
            })

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

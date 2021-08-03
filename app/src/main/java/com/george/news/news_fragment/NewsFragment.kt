package com.george.news.news_fragment

import android.content.Context
import android.net.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.george.news.databinding.NewsFragmentBinding
import com.george.news.network.NewsDataSource
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class NewsFragment : Fragment() {

    private lateinit var binding: NewsFragmentBinding
    private val newsViewModel: NewsViewModel by viewModels()
    private lateinit var postAdapter: NewsRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = NewsFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = newsViewModel

        // Init postAdapter
        postAdapter = NewsRecyclerViewAdapter(NewsRecyclerViewAdapter.OnClickListener {

            findNavController().navigate(
                NewsFragmentDirections.actionNewsFragmentToDetailsFragment2(
                    it.title,
                    it.content,
                    it.urlToImage
                )
            )
        })

        binding.newsRecyclerView.apply {
            adapter = postAdapter
        }

        observeViewModel()

        // Check for internet connection and through a Toast on unavailability
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        if(!isConnected){
            Toast.makeText(requireActivity(),"Network unavailable",Toast.LENGTH_LONG).show()
        }

        return binding.root
    }

    /**
     * Function to observe [LiveData]
     */
    private fun observeViewModel() {
        // Observe postListNews
        newsViewModel.postListNews?.observe(requireActivity(), {
            postAdapter.submitList(it)
        })

        // Observe listSize to handle errorTextView's and progressBar's visibility
        NewsDataSource.listSize.observe(requireActivity(), { size ->
            when {
                size == 0 -> {
                    binding.errorTextView.visibility = View.VISIBLE
                    binding.statusImage.visibility = View.GONE
                }
                size >= 1 -> {
                    binding.statusImage.visibility = View.GONE
                    binding.errorTextView.visibility = View.GONE
                }
                size == -1 -> {
                    binding.statusImage.visibility = View.VISIBLE
                    binding.errorTextView.visibility = View.GONE
                }
            }
        })
    }


    override fun onResume() {
        super.onResume()
        binding.newsRecyclerView.addOnScrollListener(listener)
    }

    override fun onPause() {
        super.onPause()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        NewsDataSource.listSize = MutableLiveData(-1)
    }

    private val listener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            binding.toolbar.translate(dy)
        }
    }
}

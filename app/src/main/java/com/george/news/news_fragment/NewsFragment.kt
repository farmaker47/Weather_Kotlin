package com.george.news.news_fragment

import android.content.Context
import android.net.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.george.news.databinding.NewsFragmentBinding
import com.george.news.network.NewsDataSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class NewsFragment : Fragment() {

    private lateinit var binding: NewsFragmentBinding
    private val newsViewModel: NewsViewModel by viewModels()
    private lateinit var postAdapter: NewsRecyclerViewAdapter
    private var isConnected:Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NewsFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = newsViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        isConnected = activeNetwork?.isConnectedOrConnecting == true
        if(!isConnected){
            Toast.makeText(requireActivity(),"Network unavailable",Toast.LENGTH_LONG).show()
        }
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
        //binding.newsRecyclerView.scrollToPosition(0)
    }

    override fun onPause() {
        super.onPause()
        binding.newsRecyclerView.removeOnScrollListener(listener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // On destroy set list size to this value
        if(isConnected){
            NewsDataSource.listSize = MutableLiveData(-1)
        }else{
            NewsDataSource.listSize = MutableLiveData(0)
        }

    }

    private val listener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            //Log.e("POSITION_0", dy.toString())

            // Uncomment to see the toolbar effect on first screen
            // binding.toolbar.translate(dy)


            /*val linearLayoutManager = binding.newsRecyclerView.layoutManager as LinearLayoutManager
            val firstVisiblePosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition()*/
            //Log.e("POSITION", firstVisiblePosition.toString())
            /*if(firstVisiblePosition == 0 || firstVisiblePosition == -1){
                lifecycleScope.launch {
                    delay(10)
                    binding.toolbar.translate(-800)
                }
            }else{
                lifecycleScope.launch {
                    delay(10)
                    binding.toolbar.translate(100)
                }
            }*/

            /*if(dy>0){
                binding.toolbar.translate(100)
            }else{
                binding.toolbar.translate(-800)
            }*/

            //binding.toolbar.translate(dy)
        }
    }
}

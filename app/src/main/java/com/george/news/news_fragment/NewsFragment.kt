package com.george.news.news_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.george.news.databinding.NewsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class NewsFragment : Fragment() {

    private lateinit var binding: NewsFragmentBinding
    private val newsViewModel: NewsViewModel by viewModels()
    private lateinit var postAdapter:NewsRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = NewsFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = newsViewModel

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

        //Passing listener and viewmodel to adapter
        //binding.weatherRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        /*binding.newsRecyclerView.adapter =
            NewsRecyclerViewAdapter(NewsRecyclerViewAdapter.OnClickListener {

                findNavController().navigate(
                    NewsFragmentDirections.actionNewsFragmentToDetailsFragment2(
                        it.title,
                        it.content,
                        it.urlToImage
                    )
                )
            })*/

        observeViewModel()

        return binding.root
    }

    private fun observeViewModel() {
        /*newsViewModel.newsData.observe(requireActivity(), {
            it.data?.articles?.get(0)?.let { it1 -> Log.v("RESULT", it1.description) }
        })*/

        newsViewModel.postListNews?.observe(requireActivity(),{
            postAdapter.submitList(it)
        })
    }


    override fun onResume() {
        super.onResume()
        binding.newsRecyclerView.addOnScrollListener(listener)
    }

    override fun onPause() {
        super.onPause()

    }

    private val listener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            binding.toolbar.translate(dy)
        }
    }
}

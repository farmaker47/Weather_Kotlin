package com.george.news.details_fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.george.news.R
import com.george.news.databinding.DetailsFragmentBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailsFragment : Fragment() {

    companion object {
        fun newInstance() = DetailsFragment()
    }

    private val args: DetailsFragmentArgs by navArgs()

    private lateinit var binding: DetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DetailsFragmentBinding.inflate(inflater, container, false)

        // Set title, content and image
        binding.titleTextView.text = args.title
        binding.contentTextView.text = args.content
        Glide.with(requireActivity()).load(args.imageURL).placeholder(R.drawable.ic_broken_image)
            .centerCrop().into(binding.imageView)

        // Use same toolbar with NewsFragment but shrink it
        lifecycleScope.launch {
            delay(10)
            binding.toolbar.translate(800)
        }

        // Navigate up by clicking back button
        binding.menu.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

}
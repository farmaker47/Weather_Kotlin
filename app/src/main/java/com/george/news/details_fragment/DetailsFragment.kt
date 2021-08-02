package com.george.news.details_fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.george.news.R
import com.george.news.databinding.DetailsFragmentBinding

class DetailsFragment : Fragment() {

    companion object {
        fun newInstance() = DetailsFragment()
    }

    private lateinit var viewModel: DetailsViewModel

    private val args: DetailsFragmentArgs by navArgs()

    private lateinit var binding: DetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DetailsFragmentBinding.inflate(inflater, container, false)

        Log.i("Details", args.title)

        binding.titleTextView.text = args.title
        binding.contentTextView.text = args.content
        Glide.with(requireActivity()).load(args.imageURL).placeholder(R.drawable.ic_broken_image).centerCrop().into(binding.imageView)


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
package com.example.dogs.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.dogs.databinding.FragmentDetailsBinding
import com.example.dogs.ui.detail.adapter.ImageAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment(){

    private lateinit var binding: FragmentDetailsBinding
    private val viewModel: DetailsViewModel by viewModels()
    private val adapter = ImageAdapter()
    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rv.adapter = adapter

        viewModel.isRefreshing.observe(viewLifecycleOwner) {
            binding.swipeContainer.isRefreshing = it
        }
        viewModel.images.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        binding.swipeContainer.setOnRefreshListener {
            viewModel.getImageUrls(args.breedId)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getImageUrls(args.breedId)
    }
}
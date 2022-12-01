package com.example.dogs.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.dogs.R
import com.example.dogs.databinding.FragmentDetailsBinding
import com.example.dogs.ui.detail.adapter.ImageAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

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

        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }
        binding.swipeContainer.setOnRefreshListener {
            viewModel.refreshImageUrls(args.breedId)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getImageUrls(args.breedId)
    }

    private fun render(viewState: DetailsViewState) {
        when (viewState) {
            Initial -> {}
            Refreshing -> binding.swipeContainer.isRefreshing = true
            is Content -> {
                binding.emptyList.isVisible = viewState.result.isEmpty()
                adapter.submitList(viewState.result)
                binding.swipeContainer.isRefreshing = false
            }
            is NetworkError -> {
                showError(viewState.message)
                binding.swipeContainer.isRefreshing = false
            }
        }
    }

    private fun showError(message: String) {
        Snackbar.make(binding.swipeContainer, message, Snackbar.LENGTH_SHORT)
            .setAction(getString(R.string.refresh)) {
                viewModel.refreshImageUrls(args.breedId)
            }
            .show()
    }
}
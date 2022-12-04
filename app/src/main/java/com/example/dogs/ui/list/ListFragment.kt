package com.example.dogs.ui.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dogs.R
import com.example.dogs.databinding.FragmentListBinding
import com.example.dogs.ui.list.adapter.BreedAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment(), BreedAdapter.Listener {

    private lateinit var binding: FragmentListBinding
    private val viewModel: ListViewModel by viewModels()
    private val adapter = BreedAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rv.adapter = adapter
        binding.swipeContainer.setOnRefreshListener {
            viewModel.refreshAllBreeds()
        }
        binding.toolbar.starBreedsButton.setOnClickListener {
            findNavController().navigate(
                ListFragmentDirections.toFavoriteDogs()
            )
        }
        binding.toolbar.starImagesButton.setOnClickListener {
            findNavController().navigate(
                ListFragmentDirections.toFavoriteImages()
            )
        }
        binding.filterEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.updateFilters(binding.filterEt.text.toString())
                (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    view.windowToken, 0
                )
                true
            } else false
        }

        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(viewState: ListViewState) {
        when (viewState) {
            Initial -> {}
            Refreshing -> binding.swipeContainer.isRefreshing = true
            is Content -> {
                binding.emptyList.isVisible = viewState.result.isEmpty()
                adapter.submitList(viewState.result)
                binding.swipeContainer.isRefreshing = false
                if(viewState.clearEditText){
                    binding.filterEt.text?.clear()
                }
            }
            is NetworkError -> {
                showError(viewState.message)
                binding.swipeContainer.isRefreshing = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllBreeds()
    }

    private fun showError(message: String) {
        Snackbar.make(binding.swipeContainer, message, Snackbar.LENGTH_SHORT)
            .setAction(getString(R.string.refresh)) {
                viewModel.refreshAllBreeds()
            }
            .show()
    }

    override fun onItemSelected(id: String) {
        findNavController().navigate(
            ListFragmentDirections.toDetails(
                breedId = id
            )
        )
    }

    override fun onItemFavoriteClicked(id: String, newIsFavorite: Boolean) {
        viewModel.updateBreedFavoriteById(id, newIsFavorite)
    }
}
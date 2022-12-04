package com.example.dogs.ui.favoriteDogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dogs.R
import com.example.dogs.databinding.FragmentFavoriteDogsBinding
import com.example.dogs.ui.list.adapter.BreedAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteDogsFragment : Fragment(), BreedAdapter.Listener {

    private lateinit var binding: FragmentFavoriteDogsBinding
    private val viewModel: FavoriteDogsViewModel by viewModels()
    private val adapter = BreedAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteDogsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rv.adapter = adapter
        binding.toolbar.starImagesButton.setOnClickListener {
            findNavController().navigate(
                FavoriteDogsFragmentDirections.toFavoriteImages()
            )
        }
        binding.toolbar.starBreedsImage.setImageResource(R.drawable.ic_star_empty)
        binding.toolbar.starBreedsButton.setOnClickListener {
            findNavController().navigate(
                FavoriteDogsFragmentDirections.toList()
            )
        }

        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllFavoriteBreeds()
    }

    private fun render(viewState: FavoriteViewState) {
        when (viewState) {
            Initial -> {}
            Refreshing -> {}
            is Content -> {
                binding.emptyList.isVisible = viewState.result.isEmpty()
                adapter.submitList(viewState.result)
            }
            is NetworkError -> {}
        }
    }

    override fun onItemSelected(id: String) {
        findNavController().navigate(
            FavoriteDogsFragmentDirections.toDetails(
                breedId = id
            )
        )
    }

    override fun onItemFavoriteClicked(id: String, newIsFavorite: Boolean) {
        viewModel.updateBreedFavoriteById(id, newIsFavorite)
    }
}
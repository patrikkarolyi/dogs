package com.example.dogs.ui.favoriteImages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.dogs.databinding.FragmentFavoriteImagesBinding
import com.example.dogs.ui.detail.adapter.ImageAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteImagesFragment : Fragment(), ImageAdapter.Listener {

    private lateinit var binding: FragmentFavoriteImagesBinding
    private val viewModel: FavoriteImagesViewModel by viewModels()
    private val adapter = ImageAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteImagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rv.adapter = adapter

        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavoriteImageUrls()
    }

    private fun render(viewState: FavoriteImagesViewState) {
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

    override fun onItemFavoriteClicked(id: String, newIsFavorite: Boolean) {
        viewModel.updateImageFavoriteById( id, newIsFavorite)
    }
}
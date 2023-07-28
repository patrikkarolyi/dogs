package com.example.dogs.ui.fav_dog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.dogs.ui.theme.DogsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavDogFragment : Fragment() {

    private val viewModel: FavDogViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                DogsTheme {
                    FavDogScreen(
                        onItemClicked = { id ->
                            onItemSelected(id)
                        },
                        onItemFavoriteClicked = { id, isFavorite ->
                            onItemFavoriteClicked(id, isFavorite)
                        },
                        onNavBack = {
                            findNavController().popBackStack()
                        }
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllFavoriteBreeds()
    }

    private fun onItemSelected(id: String) {
        findNavController().navigate(
            FavDogFragmentDirections.toDetails(
                breedId = id
            )
        )
    }

    private fun onItemFavoriteClicked(id: String, newIsFavorite: Boolean) {
        viewModel.updateBreedFavoriteById(id, newIsFavorite)
    }
}
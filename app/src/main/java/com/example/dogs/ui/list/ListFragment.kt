package com.example.dogs.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dogs.navigation.NavDirection
import com.example.dogs.ui.theme.DogsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment() {

    private val viewModel: ListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                DogsTheme {
                    ListScreen(
                        onItemClicked = { id ->
                            onItemSelected(id)
                        },
                        onItemFavoriteClicked = { id, isFavorite ->
                            onItemFavoriteClicked(id, isFavorite)
                        }
                    )
                }
            }
        }
    }

    private fun navigateTo(navDirection: NavDirection) {
        findNavController().navigate(
            when (navDirection) {
                NavDirection.ToFavoriteDogs -> ListFragmentDirections.toFavoriteDogs()
                NavDirection.ToFavoriteImages -> ListFragmentDirections.toFavoriteImages()
            }
        )
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllBreeds()
    }

    private fun onItemSelected(id: String) {
        findNavController().navigate(
            ListFragmentDirections.toDetails(
                breedId = id
            )
        )
    }

    private fun onItemFavoriteClicked(id: String, newIsFavorite: Boolean) {
        viewModel.updateBreedFavoriteById(id, newIsFavorite)
    }
}
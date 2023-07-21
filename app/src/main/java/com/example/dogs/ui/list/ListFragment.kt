package com.example.dogs.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.dogs.ui.list.NavDirection.ToFavoriteDogs
import com.example.dogs.ui.list.NavDirection.ToFavoriteImages
import com.example.dogs.ui.list.NavDirection.ToSearch
import com.example.dogs.ui.list.adapter.BreedAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListFragment : Fragment(), BreedAdapter.Listener {

    private val viewModel: ListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ListScreen(onItemClicked = { id -> onItemSelected(id) })
            }
        }
    }

    private fun navigateTo(navDirection: NavDirection) {
        findNavController().navigate(
            when (navDirection) {
                ToSearch -> ListFragmentDirections.toFavoriteDogs()
                ToFavoriteDogs -> ListFragmentDirections.toFavoriteDogs()
                ToFavoriteImages -> ListFragmentDirections.toFavoriteImages()
            }
        )
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllBreeds()
        lifecycleScope.launch {
            viewModel.navDirection.collectLatest {
                navigateTo(it)
            }
        }
    }

    override fun onItemSelected(id: String) {
        findNavController().navigate(
            ListFragmentDirections.toDetails(
                breedId = id
            )
        )
    }

    override fun onItemFavoriteClicked(id: String, newIsFavorite: Boolean) {}
}
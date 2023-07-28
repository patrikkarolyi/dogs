package com.example.dogs.ui.fav_img

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.dogs.ui.detail.DetailsScreen
import com.example.dogs.ui.theme.DogsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavImgFragment : Fragment() {

    private val viewModel: FavImgViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                DogsTheme {
                    DetailsScreen(
                        onItemFavoriteClicked = { id, isFavorite ->
                            onItemFavoriteClicked( id, isFavorite )
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
        viewModel.getFavoriteImageUrls()
    }

    private fun onItemFavoriteClicked(id: String, newIsFavorite: Boolean) {
        viewModel.updateImageFavoriteById(id, newIsFavorite)
    }

    private fun onItemTextClicked(breedId: String) {
        //binding.filterEt.setText(breedId)
        viewModel.updateFilters(breedId)
    }
}
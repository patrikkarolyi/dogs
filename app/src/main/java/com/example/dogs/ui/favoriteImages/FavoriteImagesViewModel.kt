package com.example.dogs.ui.favoriteImages

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogs.data.DogRepository
import com.example.dogs.ui.favoriteImages.FavoriteImagesViewState.Content
import com.example.dogs.ui.favoriteImages.FavoriteImagesViewState.Initial
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoriteImagesViewModel @Inject constructor(
    private val dataSource: DogRepository,
) : ViewModel() {

    var uiState by mutableStateOf<FavoriteImagesViewState>(Initial)
        private set

    fun getFavoriteImageUrls() {
        viewModelScope.launch {
            uiState = withContext(Dispatchers.IO) {
                Content(dataSource.getAllFavoriteImages())
            }
        }
    }

    fun updateImageFavoriteById(id: String, newIsFavorite: Boolean) {
        viewModelScope.launch {
            uiState = withContext(Dispatchers.IO) {
                dataSource.updateImageFavoriteById(id, newIsFavorite)
                Content(dataSource.getAllFavoriteImages())

            }
        }
    }

    fun updateFilters(filter: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Content(
                    result = dataSource.getAllFavoriteImages()
                        .asSequence()
                        .filter { it.breedId.contains(filter) }
                        .toList()
                )
            }
        }
    }
}
package com.example.dogs.ui.favorite

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogs.data.ImageRepository
import com.example.dogs.ui.favorite.FavImgViewState.Content
import com.example.dogs.ui.favorite.FavImgViewState.Initial
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavImgViewModel @Inject constructor(
    private val dataSource: ImageRepository,
) : ViewModel() {

    var uiState by mutableStateOf<FavImgViewState>(Initial)
        private set

    private var currentFilter by mutableStateOf("")

    fun getFavoriteImageUrls() {
        viewModelScope.launch {
            uiState = withContext(Dispatchers.IO) {
                Content(
                    dataSource.getAllFavoriteImages()
                        .asSequence()
                        .filter { it.breedId.contains(currentFilter) }
                        .toList(),
                )
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
            currentFilter = filter
            getFavoriteImageUrls()
        }
    }
}
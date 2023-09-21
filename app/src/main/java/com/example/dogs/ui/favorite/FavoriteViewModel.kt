package com.example.dogs.ui.favorite

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogs.data.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val imageDataSource: ImageRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val currentFilter = savedStateHandle.getStateFlow("current_filter_favorite", "")

    val uiState: StateFlow<FavoriteViewContent> =
        combine(
            imageDataSource.observeAllFavoriteImages(),
            currentFilter
        ) { images, filter ->
            images.filter { item -> item.breedId.contains(filter) }
        }
            .map(::FavoriteViewContent)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = FavoriteViewContent(),
            )


    fun updateImageFavoriteById(id: String, newIsFavorite: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                imageDataSource.updateImageFavoriteById(id, newIsFavorite)
            }
        }
    }

    fun updateFilters(filter: String) {
        viewModelScope.launch {
            savedStateHandle["current_filter_favorite"] = filter
        }
    }
}
package com.example.dogs.ui.favorite

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogs.data.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val imageDataSource: ImageRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val currentFilter = savedStateHandle.getStateFlow(currentFilterFlow, "")

    private val _images = imageDataSource.observeAllFavoriteImages()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )


    private val _uiState = MutableStateFlow(FavoriteViewContent())
    val uiState: StateFlow<FavoriteViewContent> =
        combine(
            _uiState,
            _images,
            currentFilter
        ) { state, images, filter ->
            state.copy(
                result = images.filter { item -> item.breedId.contains(filter) },
            )
        }.stateIn(
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
            savedStateHandle[currentFilterFlow] = filter
        }
    }
    companion object{
        const val currentFilterFlow = "current_filter_flow"
    }
}
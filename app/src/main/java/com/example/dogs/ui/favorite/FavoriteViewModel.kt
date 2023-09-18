package com.example.dogs.ui.favorite

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogs.data.ImageRepository
import com.example.dogs.data.disk.model.RoomImageData
import com.example.dogs.ui.favorite.FavoriteViewState.Initial
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
    private val dataSource: ImageRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val currentFilter = savedStateHandle.getStateFlow("current_filter_favorite", "")

    val uiState: StateFlow<FavoriteViewState> = combine(
        dataSource.observeAllFavoriteImages(),
        currentFilter,
    ) { list, filter ->
        list.filter { item -> item.breedId.contains(filter) }
    }
        .map<List<RoomImageData>, FavoriteViewState>(FavoriteViewState::Content)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Initial,
        )

    fun updateImageFavoriteById(id: String, newIsFavorite: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dataSource.updateImageFavoriteById(id, newIsFavorite)
            }
        }
    }

    fun updateFilters(filter: String) {
        viewModelScope.launch {
            savedStateHandle["current_filter_favorite"] = filter
        }
    }
}
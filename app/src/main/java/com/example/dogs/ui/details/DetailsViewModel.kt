package com.example.dogs.ui.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogs.data.ImageRepository
import com.example.dogs.data.disk.model.RoomImageData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val dataSource: ImageRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val currentBreedId = savedStateHandle.getStateFlow("current_breedId", "")

    private val _uiState = MutableStateFlow<DetailViewState>(DetailViewState.Initial)
    val uiState = _uiState

    var isRefreshing by mutableStateOf(false)
        private set

    var errorMessage = MutableSharedFlow<String>()
        private set

    fun refreshImageUrls(breedId: String) {
        viewModelScope.launch {
            isRefreshing = true
            withContext(Dispatchers.IO) {
                savedStateHandle["current_breedId"] = breedId
                dataSource.downloadImagesByBreedId(breedId)
                dataSource.observeImagesByBreedId(breedId)
                    .map<List<RoomImageData>, DetailViewState>(DetailViewState::Content)
                    .catch { exception ->
                        errorMessage.emit(exception.message ?: "error")
                    }
                    .collect {
                        _uiState.value = it
                    }
            }
            isRefreshing = false
        }
    }

    fun updateImageFavoriteById(url: String, newIsFavorite: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dataSource.updateImageFavoriteById(url, newIsFavorite)
            }
        }
    }
}
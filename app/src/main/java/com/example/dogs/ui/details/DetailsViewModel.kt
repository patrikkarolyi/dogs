package com.example.dogs.ui.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogs.data.ImageRepository
import com.example.dogs.data.network.model.NetworkResponse
import com.example.dogs.data.network.model.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val imageDataSource: ImageRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val currentBreedId = savedStateHandle.getStateFlow("current_breedId", "")

    val uiState: StateFlow<DetailsViewContent> =
        combine(
            imageDataSource.observeAllImages(),
            currentBreedId
        ) { images, breedId ->
            images.filter { image -> image.breedId == breedId }
        }
            .map(::DetailsViewContent)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = DetailsViewContent(),
            )

    var isRefreshing by mutableStateOf(false)
        private set

    var errorMessage = MutableSharedFlow<String>()
        private set

    fun refreshImageUrls(breedId: String = currentBreedId.value) {
        viewModelScope.launch {
            isRefreshing = true
            withContext(Dispatchers.IO) {
                savedStateHandle["current_breedId"] = breedId
                imageDataSource.downloadImagesByBreedId(currentBreedId.value).handleError()
            }
            isRefreshing = false
        }
    }

    fun updateImageFavoriteById(url: String, newIsFavorite: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                imageDataSource.updateImageFavoriteById(url, newIsFavorite)
            }
        }
    }

    private fun NetworkResponse<*>.handleError() {
        if (this !is NetworkResult) {
            viewModelScope.launch {
                //TODO error handling
                errorMessage.emit(this.toString())
            }
        }
    }
}
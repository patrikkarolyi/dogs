package com.example.dogs.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogs.data.network.model.NetworkResult
import com.example.dogs.data.network.model.translateNetworkResponse
import com.example.dogs.data.presentation.NetworkErrorPresentationModel
import com.example.dogs.data.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val imageRepository: ImageRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val currentBreedId = savedStateHandle.getStateFlow(currentBreedIdFlow, "")

    private val _uiState = MutableStateFlow(DetailsViewContent())
    val uiState: StateFlow<DetailsViewContent> =
        combine(
            _uiState,
            imageRepository.observeAllImages(),
            currentBreedId
        ) { state, images, breedId ->
            state.copy(
                result = images.filter { image -> image.breedId == breedId },
                isRefreshing = false
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DetailsViewContent(),
        )

    var errorMessage = MutableSharedFlow<NetworkErrorPresentationModel>()
        private set

    fun refreshImageUrls(breedId: String = currentBreedId.value) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isRefreshing = true
                )
            }
            savedStateHandle[currentBreedIdFlow] = breedId
            imageRepository.downloadImagesByBreedId(currentBreedId.value).let { result ->
                if (result !is NetworkResult) {
                    errorMessage.emit(result.translateNetworkResponse())
                }
            }
        }
    }

    fun updateImageFavoriteByUrl(url: String, newIsFavorite: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                imageRepository.updateImageFavoriteByUrl(url, newIsFavorite)
            }
        }
    }

    companion object {
        const val currentBreedIdFlow = "current_breed_id_details_flow"
    }
}
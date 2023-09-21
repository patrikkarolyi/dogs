package com.example.dogs.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogs.data.ImageRepository
import com.example.dogs.data.network.model.NetworkResponse
import com.example.dogs.data.network.model.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val currentBreedId = savedStateHandle.getStateFlow(currentBreedIdFlow, "")

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


    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    var errorMessage = MutableSharedFlow<String>()
        private set

    fun refreshImageUrls(breedId: String = currentBreedId.value) {
        viewModelScope.launch {
            _isRefreshing.emit(true)
            withContext(Dispatchers.IO) {
                savedStateHandle[currentBreedIdFlow] = breedId
                imageDataSource.downloadImagesByBreedId(currentBreedId.value).handleError()
            }
            _isRefreshing.emit(false)
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
    companion object{
        const val currentBreedIdFlow = "current_breed_id_details_flow"
    }
}
package com.example.dogs.ui.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogs.data.DogRepository
import com.example.dogs.data.ImageRepository
import com.example.dogs.network.model.NetworkResult
import com.example.dogs.ui.common.model.ImageViewState
import com.example.dogs.util.fullName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val imageDataSource: ImageRepository,
    private val dogDataSource: DogRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val currentBreedId = savedStateHandle.getStateFlow("current_breedId", "")

    private val _uiState = MutableStateFlow<DetailViewState>(DetailViewState.Initial)
    val uiState = _uiState

    var isRefreshing by mutableStateOf(false)
        private set

    var errorMessage = MutableSharedFlow<String>()
        private set

    var title by mutableStateOf("")
        private set

    fun refreshImageUrls(breedId: String = currentBreedId.value) {
        viewModelScope.launch {
            isRefreshing = true
            withContext(Dispatchers.IO) {
                savedStateHandle["current_breedId"] = breedId
                getDogNameTitle()
                downloadImages()
                getAllImages()
            }
            isRefreshing = false
        }
    }

    private fun getDogNameTitle() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                title = dogDataSource.getDogById(currentBreedId.value).fullName()
            }
        }
    }

    private suspend fun downloadImages() {
        imageDataSource.downloadImagesByBreedId(currentBreedId.value).let { result ->
            if (result !is NetworkResult) {
                //TODO error handling
                errorMessage.emit(result.toString())
            }
        }
    }

    private fun getAllImages() {
        //TODO check this
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                combine(
                    imageDataSource.observeImagesByBreedId(currentBreedId.value),
                    dogDataSource.observeAllBreeds()
                ) { images, dogs ->
                    images.map { roomItem ->
                        ImageViewState(
                            url = roomItem.url,
                            isFavorite = roomItem.isFavorite,
                            fullName = dogs.first { dog -> roomItem.breedId == dog.id }.fullName()
                        )
                    }
                }

                    .map<List<ImageViewState>, DetailViewState>(DetailViewState::Content)
                    .stateIn(
                        scope = viewModelScope,
                        started = SharingStarted.WhileSubscribed(5_000),
                        initialValue = DetailViewState.Initial,
                    )
                    .collectLatest {
                        _uiState.value = it
                    }
            }
        }
    }

    fun updateImageFavoriteById(url: String, newIsFavorite: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                imageDataSource.updateImageFavoriteById(url, newIsFavorite)
            }
        }
    }
}
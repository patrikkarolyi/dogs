package com.example.dogs.ui.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogs.data.ImageRepository
import com.example.dogs.data.disk.model.RoomImageData
import com.example.dogs.network.model.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    fun refreshImageUrls(breedId: String = currentBreedId.value) {
        viewModelScope.launch {
            isRefreshing = true
            withContext(Dispatchers.IO) {
                savedStateHandle["current_breedId"] = breedId
                downloadImages()
                getAllImages()
            }
            isRefreshing = false
        }
    }

    private suspend fun downloadImages() {
        dataSource.downloadImagesByBreedId(currentBreedId.value).let { result ->
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
                dataSource.observeImagesByBreedId(currentBreedId.value)
                    .map<List<RoomImageData>, DetailViewState>(DetailViewState::Content)
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
                dataSource.updateImageFavoriteById(url, newIsFavorite)
            }
        }
    }
}
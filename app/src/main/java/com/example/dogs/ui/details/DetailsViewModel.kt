package com.example.dogs.ui.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogs.data.ImageRepository
import com.example.dogs.network.model.NetworkIOError
import com.example.dogs.network.model.NetworkNoResult
import com.example.dogs.network.model.NetworkUnavailable
import com.example.dogs.ui.details.DetailViewState.Content
import com.example.dogs.ui.details.DetailViewState.Initial
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val dataSource: ImageRepository,
) : ViewModel() {

    var uiState by mutableStateOf<DetailViewState>(Initial)
        private set

    var isRefreshing by mutableStateOf(false)
        private set

    var errorMessage = MutableSharedFlow<String>()
        private set

    fun getImageUrls(id: String) {
        viewModelScope.launch {
            val tempState = withContext(Dispatchers.IO) {
                Content(dataSource.getImagesByBreedId(id), id)
            }
            if (tempState.result.isEmpty()) {
                refreshImageUrls(id)
            } else {
                uiState = tempState
            }
        }
    }

    fun refreshImageUrls(id: String) {
        viewModelScope.launch {
            isRefreshing = true
            uiState = withContext(Dispatchers.IO) {
                when (dataSource.downloadImagesByBreedId(id)) {
                    is NetworkNoResult -> errorMessage.emit("HTTP error")
                    NetworkIOError -> errorMessage.emit("IO error")
                    NetworkUnavailable -> errorMessage.emit("No internet")
                    else -> {}
                }
                Content(dataSource.getImagesByBreedId(id), id)
            }
            isRefreshing = false
        }
    }

    fun updateImageFavoriteById(breedId: String, url: String, newIsFavorite: Boolean) {
        viewModelScope.launch {
            uiState = withContext(Dispatchers.IO) {
                dataSource.updateImageFavoriteById(url, newIsFavorite)
                Content(dataSource.getImagesByBreedId(breedId), breedId)
            }
        }
    }
}
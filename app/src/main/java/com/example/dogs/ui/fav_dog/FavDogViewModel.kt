package com.example.dogs.ui.fav_dog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogs.data.DogRepository
import com.example.dogs.ui.fav_dog.FavDogViewState.Content
import com.example.dogs.ui.fav_dog.FavDogViewState.Initial
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavDogViewModel @Inject constructor(
    private val dataSource: DogRepository,
) : ViewModel() {

    var uiState by mutableStateOf<FavDogViewState>(Initial)
        private set


    fun getAllFavoriteBreeds() {
        viewModelScope.launch {
            uiState = withContext(Dispatchers.IO) {
                Content(dataSource.getAllFavoriteBreeds())
            }
        }
    }

    fun updateFilters(filter: String) {
        viewModelScope.launch {
            uiState = withContext(Dispatchers.IO) {
                Content(
                    result = dataSource.getAllBreeds()
                        .asSequence()
                        .filter { it.id.contains(filter) }
                        .toList(),
                )
            }
        }
    }

    fun updateBreedFavoriteById(id: String, newIsFavorite: Boolean) {
        viewModelScope.launch {
            uiState = withContext(Dispatchers.IO) {
                dataSource.updateBreedFavoriteById(id, newIsFavorite)
                Content(dataSource.getAllFavoriteBreeds())
            }
        }
    }
}
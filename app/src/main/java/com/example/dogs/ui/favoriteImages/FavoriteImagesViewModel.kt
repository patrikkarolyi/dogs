package com.example.dogs.ui.favoriteImages

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogs.data.DogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoriteImagesViewModel @Inject constructor(
    private val dataSource: DogRepository,
) : ViewModel() {

    val state: MutableLiveData<FavoriteImagesViewState> by lazy {
        MutableLiveData<FavoriteImagesViewState>(Initial)
    }

    fun getFavoriteImageUrls() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val newState = Content(dataSource.getAllFavoriteImages())

                withContext(Dispatchers.Main) {
                    state.value = newState
                }
            }
        }
    }

    fun updateImageFavoriteById(id: String, newIsFavorite: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                dataSource.updateImageFavoriteById(id, newIsFavorite)

                val newState = Content(dataSource.getAllFavoriteImages())

                withContext(Dispatchers.Main) {
                    state.value = newState
                }
            }
        }
    }
}
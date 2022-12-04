package com.example.dogs.ui.favoriteDogs

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
class FavoriteDogsViewModel @Inject constructor(
    private val dataSource: DogRepository,
) : ViewModel() {

    val state: MutableLiveData<FavoriteViewState> by lazy {
        MutableLiveData<FavoriteViewState>(Initial)
    }

    fun getAllFavoriteBreeds() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val newState = Content(dataSource.getAllFavoriteBreeds())


                withContext(Dispatchers.Main) {
                    state.value = newState
                }
            }
        }
    }

    fun updateBreedFavoriteById(id: String, newIsFavorite: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                dataSource.updateBreedFavoriteById(id, newIsFavorite)

                val newState = Content(dataSource.getAllFavoriteBreeds())

                withContext(Dispatchers.Main) {
                    state.value = newState
                }
            }
        }
    }
}
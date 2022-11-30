package com.example.dogs.ui.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogs.data.DogRepository
import com.example.dogs.data.disk.model.RoomBreedData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val dataSource: DogRepository,
) : ViewModel() {

    val breeds: MutableLiveData<List<RoomBreedData>> by lazy {
        MutableLiveData<List<RoomBreedData>>()
    }

    val isRefreshing: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun getAllFavoriteBreeds() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val value = dataSource.getAllFavoriteBreeds()
                withContext(Dispatchers.Main) {
                    breeds.value = value
                }
            }
        }
    }

    fun refreshAllBreeds() {
        viewModelScope.launch {
            isRefreshing.value = true
            withContext(Dispatchers.IO) {
                dataSource.downloadAllBreeds()
                val value = dataSource.getAllFavoriteBreeds()
                withContext(Dispatchers.Main) {
                    if (value.isNotEmpty()) {
                        breeds.value = value
                    }
                }
            }
            isRefreshing.value = false
        }
    }

    fun updateBreedFavoriteById(id: String, newIsFavorite: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dataSource.updateBreedFavoriteById(id,newIsFavorite)
                val value = dataSource.getAllFavoriteBreeds()
                withContext(Dispatchers.Main) {
                    breeds.value = value
                }
            }
        }
    }
}
package com.example.dogs.ui.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogs.data.DogRepository
import com.example.dogs.network.model.NetworkHttpError
import com.example.dogs.network.model.NetworkIOError
import com.example.dogs.network.model.NetworkResult
import com.example.dogs.network.model.NetworkUnavailable
import com.example.dogs.ui.list.ListViewState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val dataSource: DogRepository,
) : ViewModel() {

    var state by mutableStateOf<ListViewState>(Initial)
        private set

    fun getAllBreeds() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val newState = Content(dataSource.getAllBreeds())

                if (newState.result.isEmpty()) {
                    refreshAllBreeds()
                } else {
                    withContext(Dispatchers.Main) {
                        state = newState
                    }
                }
            }
        }
    }

    fun refreshAllBreeds() {
        viewModelScope.launch {

            state = Refreshing

            withContext(Dispatchers.IO) {

                val newState = when (dataSource.downloadAllBreeds()) {
                    is NetworkHttpError -> NetworkError("HTTP error")
                    NetworkIOError -> NetworkError("IO error")
                    NetworkUnavailable -> NetworkError("No internet")
                    is NetworkResult -> Content(dataSource.getAllBreeds())
                }

                withContext(Dispatchers.Main) {
                    state = newState
                }
            }
        }
    }

    fun updateBreedFavoriteById(id: String, newIsFavorite: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                dataSource.updateBreedFavoriteById(id, newIsFavorite)

                val newState = Content(dataSource.getAllBreeds())

                withContext(Dispatchers.Main) {
                    state = newState
                }
            }
        }
    }

    fun updateFilters(filter: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val newState = Content(
                    result = dataSource.getAllBreeds()
                        .asSequence()
                        .filter { it.id.contains(filter) } // blank text is always contained
                        .toList(),
                    clearEditText = false
                )

                withContext(Dispatchers.Main) {
                    state = newState
                }
            }
        }
    }
}
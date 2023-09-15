package com.example.dogs.ui.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogs.data.DogRepository
import com.example.dogs.network.model.NetworkIOError
import com.example.dogs.network.model.NetworkNoResult
import com.example.dogs.network.model.NetworkUnavailable
import com.example.dogs.ui.list.ListViewState.Content
import com.example.dogs.ui.list.ListViewState.Initial
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val dataSource: DogRepository,
) : ViewModel() {

    var uiState by mutableStateOf<ListViewState>(Initial)
        private set

    var isRefreshing by mutableStateOf(false)
        private set

    var errorMessage = MutableSharedFlow<String>()
        private set

    private var currentFilter by mutableStateOf("")

    fun getAllBreeds() {
        viewModelScope.launch {
            uiState = withContext(Dispatchers.IO) {
                Content(
                    dataSource.getAllBreeds()
                        .apply {
                            if (isEmpty()) {
                                refreshAllBreeds()
                            }
                        }
                        .asSequence()
                        .filter { it.id.contains(currentFilter) }
                        .toList(),
                )
            }
        }
    }

    fun refreshAllBreeds() {
        viewModelScope.launch {
            isRefreshing = true
            uiState = withContext(Dispatchers.IO) {
                when (dataSource.downloadAllBreeds()) {
                    is NetworkNoResult -> errorMessage.emit("HTTP error")
                    NetworkIOError -> errorMessage.emit("IO error")
                    NetworkUnavailable -> errorMessage.emit("No internet")
                    else -> {}
                }
                Content(
                    dataSource.getAllBreeds()
                        .asSequence()
                        .filter { it.id.contains(currentFilter) }
                        .toList(),
                )
            }
            isRefreshing = false
        }
    }

    fun updateBreedFavoriteById(id: String, newIsFavorite: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dataSource.updateBreedFavoriteById(id, newIsFavorite)
            }
            getAllBreeds()
        }
    }

    fun updateFilters(filter: String) {
        viewModelScope.launch {
            currentFilter = filter
            getAllBreeds()
        }
    }
}
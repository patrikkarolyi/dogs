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

    fun getAllBreeds() {
        viewModelScope.launch {
            val tempState = withContext(Dispatchers.IO) {
                Content(dataSource.getAllBreeds())
            }
            if(tempState.result.isEmpty()){
                refreshAllBreeds()
            }
            else{
                uiState = tempState
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
                Content(dataSource.getAllBreeds())
            }
            isRefreshing = false
        }
    }

    fun updateBreedFavoriteById(id: String, newIsFavorite: Boolean) {
        viewModelScope.launch {
            uiState = withContext(Dispatchers.IO) {
                dataSource.updateBreedFavoriteById(id, newIsFavorite)
                Content(dataSource.getAllBreeds())
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
}
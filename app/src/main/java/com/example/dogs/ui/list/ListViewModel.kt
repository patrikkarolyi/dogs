package com.example.dogs.ui.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogs.data.DogRepository
import com.example.dogs.data.disk.model.RoomBreedData
import com.example.dogs.ui.list.ListViewState.Initial
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val dataSource: DogRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val currentFilter = savedStateHandle.getStateFlow("current_filter", "")

    val uiState: StateFlow<ListViewState> =
        combine(
            dataSource.observeAllBreeds(),
            currentFilter,
        ) { list, filter ->
            list.filter { item -> item.id.contains(filter) }
        }
            .map<List<RoomBreedData>, ListViewState>(ListViewState::Content)
            .onStart {
                refreshAllBreeds()
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = Initial,
            )


    var isRefreshing by mutableStateOf(false)
        private set

    var errorMessage = MutableSharedFlow<String>()
        private set


    fun getAllBreeds() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dataSource.observeAllBreeds()
            }
        }
    }

    fun refreshAllBreeds() {
        viewModelScope.launch {
            isRefreshing = true
            withContext(Dispatchers.IO) {
                dataSource.downloadAllBreeds()
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
            savedStateHandle["current_filter"] = filter
            getAllBreeds()
        }
    }
}
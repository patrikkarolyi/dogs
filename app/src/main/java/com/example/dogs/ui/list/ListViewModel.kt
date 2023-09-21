package com.example.dogs.ui.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogs.data.DogRepository
import com.example.dogs.data.network.model.NetworkResponse
import com.example.dogs.data.network.model.NetworkResult
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
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val currentFilter = savedStateHandle.getStateFlow("current_filter_list", "")

    val uiState: StateFlow<ListViewContent> =
        combine(
            dataSource.observeAllBreeds(),
            currentFilter,
        ) { list, filter ->
            list.filter { item -> item.breedId.contains(filter) }
        }
            .map(::ListViewContent)
            .onStart { refreshAllBreeds() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ListViewContent(),
            )

    var isRefreshing by mutableStateOf(false)
        private set

    var errorMessage = MutableSharedFlow<String>()
        private set

    fun refreshAllBreeds() {
        viewModelScope.launch {
            isRefreshing = true
            withContext(Dispatchers.IO) {
                dataSource.downloadAllBreeds().handleError()
            }
            isRefreshing = false
        }
    }

    fun updateFilters(filter: String) {
        viewModelScope.launch {
            savedStateHandle["current_filter_list"] = filter
        }
    }

    private fun NetworkResponse<*>.handleError() {
        if (this !is NetworkResult) {
            viewModelScope.launch {
                //TODO error handling
                errorMessage.emit(this.toString())
            }
        }
    }
}
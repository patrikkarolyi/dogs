package com.example.dogs.ui.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogs.data.network.model.NetworkResponse
import com.example.dogs.data.network.model.NetworkResult
import com.example.dogs.data.network.model.translateNetworkResponse
import com.example.dogs.data.presentation.DogPresentationModel
import com.example.dogs.data.presentation.NetworkErrorPresentationModel
import com.example.dogs.data.repository.DogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val dogRepository: DogRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val currentFilter = savedStateHandle.getStateFlow(currentFilterListFlow, "")

    private val _dogs: StateFlow<List<DogPresentationModel>> = dogRepository.observeAllBreeds()
        .onStart { refreshAllBreeds() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )


    private val _uiState = MutableStateFlow(ListViewContent())
    val uiState =
        combine(
            _uiState,
            _dogs,
            currentFilter
        ) { state, list, filter ->
            state.copy(
                result = list.filter { item -> item.breedId.contains(filter) },
                isRefreshing = false
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ListViewContent())

    var errorMessage = MutableSharedFlow<NetworkErrorPresentationModel>()
        private set

    fun refreshAllBreeds() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isRefreshing = true
                )
            }
            dogRepository.downloadAllBreeds().handleError()
        }
    }

    fun updateFilters(filter: String) {
        viewModelScope.launch {
            savedStateHandle[currentFilterListFlow] = filter
        }
    }

    private fun NetworkResponse<*>.handleError() {
        if (this !is NetworkResult) {
            viewModelScope.launch {
                errorMessage.emit(
                    this@handleError.translateNetworkResponse()
                )
            }
        }
    }

    companion object {
        const val currentFilterListFlow = "current_filter_list_flow"
    }
}
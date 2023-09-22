package com.example.dogs.ui.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogs.data.DogRepository
import com.example.dogs.data.network.model.NetworkResponse
import com.example.dogs.data.network.model.NetworkResult
import com.example.dogs.data.network.model.translateNetworkResponse
import com.example.dogs.data.presentation.NetworkErrorPresentationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    val currentFilter = savedStateHandle.getStateFlow(currentFilterListFlow, "")

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

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    var errorMessage = MutableSharedFlow<NetworkErrorPresentationModel>()
        private set

    fun refreshAllBreeds() {
        viewModelScope.launch {
            _isRefreshing.emit(true)
            withContext(Dispatchers.IO) {
                dataSource.downloadAllBreeds().handleError()
            }
            _isRefreshing.emit(false)
        }
    }

    fun updateFilters(filter: String) {
        viewModelScope.launch {
            savedStateHandle[currentFilterListFlow] = filter
        }
    }

    private fun NetworkResponse<*>.handleError() {
        if(this !is NetworkResult){
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
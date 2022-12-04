package com.example.dogs.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogs.data.DogRepository
import com.example.dogs.network.model.NetworkHttpError
import com.example.dogs.network.model.NetworkIOError
import com.example.dogs.network.model.NetworkResult
import com.example.dogs.network.model.NetworkUnavailable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val dataSource: DogRepository,
) : ViewModel() {

    val state: MutableLiveData<ListViewState> by lazy {
        MutableLiveData<ListViewState>(Initial)
    }

    fun getAllBreeds() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val newState = Content(dataSource.getAllBreeds())

                if (newState.result.isEmpty()) {
                    refreshAllBreeds()
                } else {
                    withContext(Dispatchers.Main) {
                        state.value = newState
                    }
                }
            }
        }
    }

    fun refreshAllBreeds() {
        viewModelScope.launch {

            state.value = Refreshing

            withContext(Dispatchers.IO) {

                val newState = when (dataSource.downloadAllBreeds()) {
                    is NetworkHttpError -> NetworkError("HTTP error")
                    NetworkIOError -> NetworkError("IO error")
                    NetworkUnavailable -> NetworkError("No internet")
                    is NetworkResult -> Content(dataSource.getAllBreeds())
                }

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

                val newState = Content(dataSource.getAllBreeds())

                withContext(Dispatchers.Main) {
                    state.value = newState
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
                        .filter {  it.id.contains(filter) } // blank text is always contained
                        .toList(),
                    clearEditText = false
                )

                withContext(Dispatchers.Main) {
                    state.value = newState
                }
            }
        }
    }
}
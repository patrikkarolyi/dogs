package com.example.dogs.ui.detail

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
class DetailsViewModel @Inject constructor(
    private val dataSource: DogRepository,
) : ViewModel() {

    val state: MutableLiveData<DetailsViewState> by lazy {
        MutableLiveData<DetailsViewState>(Initial)
    }

    fun getImageUrls(id: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val newState = Content(dataSource.getImagesByBreedId(id).map { it.url })

                if (newState.result.isEmpty()) {
                    refreshImageUrls(id)
                } else {
                    withContext(Dispatchers.Main) {
                        state.value = newState
                    }
                }
            }
        }
    }

    fun refreshImageUrls(id: String) {
        viewModelScope.launch {

            state.value = Refreshing

            withContext(Dispatchers.IO) {

                val newState = when (dataSource.downloadImagesByBreedId(id)) {
                    is NetworkHttpError -> NetworkError("HTTP error")
                    NetworkIOError -> NetworkError("IO error")
                    NetworkUnavailable -> NetworkError("No internet")
                    is NetworkResult -> Content(dataSource.getImagesByBreedId(id).map { it.url })
                }

                withContext(Dispatchers.Main) {
                    state.value = newState
                }
            }
        }
    }
}
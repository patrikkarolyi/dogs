package com.example.dogs.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogs.data.DogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val dataSource: DogRepository,
) : ViewModel() {

    val images: MutableLiveData<List<String>> by lazy {
        MutableLiveData<List<String>>()
    }

    val isRefreshing: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun getImageUrls(id: String) {
        isRefreshing.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val value = dataSource.getImagesByBreedId(id).map{it.url}
                withContext(Dispatchers.Main){
                    images.value = value
                }
            }
        }
        isRefreshing.value = false
    }

}
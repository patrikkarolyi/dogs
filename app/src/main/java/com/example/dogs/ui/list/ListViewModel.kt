package com.example.dogs.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogs.data.DogRepository
import com.example.dogs.data.disk.model.RoomBreedData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val dataSource: DogRepository,
) : ViewModel() {

    val breeds: MutableLiveData<List<RoomBreedData>> by lazy {
        MutableLiveData<List<RoomBreedData>>()
    }

    fun getAllBreeds() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                dataSource.downloadAllBreeds()
                val value = dataSource.getAllBreeds()
                withContext(Dispatchers.Main){
                    breeds.value = value
                }
            }
        }
    }
}
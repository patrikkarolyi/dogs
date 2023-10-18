package com.example.dogs.data.repository

import com.example.dogs.data.network.model.AllBreedData
import com.example.dogs.data.network.model.NetworkIOError
import com.example.dogs.data.network.model.NetworkResponse
import com.example.dogs.data.presentation.DogPresentationModel
import com.example.dogs.data.test_data.dogTestData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeDogRepository : DogRepository {

    private val dogs = MutableStateFlow<List<DogPresentationModel>>(emptyList())

    override fun observeAllBreeds(): Flow<List<DogPresentationModel>> {
        return dogs
    }

    override suspend fun downloadAllBreeds(): NetworkResponse<AllBreedData> {
        dogs.emit(dogTestData)
        return NetworkIOError
    }
}
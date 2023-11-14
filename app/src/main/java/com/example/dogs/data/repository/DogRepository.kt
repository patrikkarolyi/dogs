package com.example.dogs.data.repository

import com.example.dogs.data.network.model.AllBreedData
import com.example.dogs.data.network.model.NetworkResponse
import com.example.dogs.data.presentation.DogPresentationModel
import kotlinx.coroutines.flow.Flow

interface DogRepository {
    fun observeAllBreeds(): Flow<List<DogPresentationModel>>

    suspend fun getAllBreeds(): List<DogPresentationModel>
    suspend fun downloadAllBreeds(): NetworkResponse<AllBreedData>
}
package com.example.dogs.data

import com.example.dogs.network.NetworkDataSource
import com.example.dogs.network.model.NetworkResult
import javax.inject.Inject

class DogRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
) {
    suspend fun getAllBreeds(): List<String> {
        val networkResponse = networkDataSource.getAllBreeds()
        if(networkResponse is NetworkResult){
            return networkResponse.result.message.keys.toList()
        }
        return listOf()
    }
}
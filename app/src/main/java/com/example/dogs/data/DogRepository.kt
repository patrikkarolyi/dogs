package com.example.dogs.data

import com.example.dogs.data.disk.DiskDataSource
import com.example.dogs.data.disk.model.RoomBreedData
import com.example.dogs.network.NetworkDataSource
import com.example.dogs.network.model.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DogRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val diskDataSource: DiskDataSource,
) {

    fun observeAllBreeds(): Flow<List<RoomBreedData>> {
        return diskDataSource.getAllBreeds()
    }

    suspend fun downloadAllBreeds(): NetworkResponse<AllBreedData> {
        return when (val networkResponse = networkDataSource.getAllBreeds()) {
            is NetworkResult -> {
                diskDataSource.insertBreeds(mapBreedsNetworkToRoom(networkResponse.result.message))
                networkResponse
            }
            else -> networkResponse
        }
    }

    private fun mapBreedsNetworkToRoom(map: Map<String, List<String>>): List<RoomBreedData> {
        val result = mutableListOf<RoomBreedData>()
        for (breed in map.keys) {
            val subBreeds = map[breed]
            if (subBreeds?.isNotEmpty() == true) {
                for (subBreed in subBreeds) {
                    result.add(
                        createRoomBreedItem(breed, subBreed)
                    )
                }
            } else {
                result.add(
                    createRoomBreedItem(breed, "")
                )
            }
        }
        return result
    }

    private fun createRoomBreedItem(breed: String, subBreed: String): RoomBreedData {
        return RoomBreedData(
            id = "$breed$subBreed",
            breedName = breed,
            subBreedName = subBreed,
            isFavorite = false
        )
    }

    fun getDogById(breedId: String): RoomBreedData {
        return diskDataSource.getBreedById(breedId)
    }
}

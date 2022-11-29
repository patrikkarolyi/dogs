package com.example.dogs.data

import com.example.dogs.data.disk.DiskDataSource
import com.example.dogs.data.disk.model.RoomBreedData
import com.example.dogs.network.NetworkDataSource
import com.example.dogs.network.model.NetworkResult
import javax.inject.Inject

class DogRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val diskDataSource: DiskDataSource,
) {

    fun getAllBreeds(): List<RoomBreedData> {
        return diskDataSource.getAllBreeds()
    }

    suspend fun downloadAllBreeds() {
        val networkResponse = networkDataSource.getAllBreeds()
        if (networkResponse is NetworkResult) {
            diskDataSource.insertBreeds(mapBreedsNetworkToRoom(networkResponse.result.message))
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
}
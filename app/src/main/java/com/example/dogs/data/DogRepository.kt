package com.example.dogs.data

import com.example.dogs.data.disk.DiskDataSource
import com.example.dogs.data.disk.model.RoomBreedData
import com.example.dogs.data.disk.model.RoomImageData
import com.example.dogs.network.NetworkDataSource
import com.example.dogs.network.model.NetworkHttpError
import com.example.dogs.network.model.NetworkIOError
import com.example.dogs.network.model.NetworkResult
import com.example.dogs.network.model.NetworkUnavailable
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
        if (networkResponse is NetworkResult) { //TODO error handling
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

    suspend fun getImagesByBreedId(id: String): List<RoomImageData> {
        val dog = diskDataSource.getBreedById(id)
        if (diskDataSource.getImagesById(id).isEmpty()) {
            val networkResult = networkDataSource.getAllUrlOfBreed(dog.breedName, dog.subBreedName)
            if(networkResult is NetworkResult) { //TODO error handling
                    diskDataSource.insertImagesObject(
                        networkResult.result.message.map {
                            RoomImageData(
                                url = it,
                                breedId = id
                            )
                        }
                    )
                }
            }
        return diskDataSource.getImagesById(id)
    }
}
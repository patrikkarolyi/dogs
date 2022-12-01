package com.example.dogs.data

import com.example.dogs.data.disk.DiskDataSource
import com.example.dogs.data.disk.model.RoomBreedData
import com.example.dogs.data.disk.model.RoomImageData
import com.example.dogs.network.NetworkDataSource
import com.example.dogs.network.model.*
import javax.inject.Inject

class DogRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val diskDataSource: DiskDataSource,
) {

    fun getAllBreeds(): List<RoomBreedData> {
        return diskDataSource.getAllBreeds()
    }

    fun getAllFavoriteBreeds(): List<RoomBreedData> {
        return diskDataSource.getAllFavoriteBreeds()
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

    suspend fun downloadImagesByBreedId(id: String): NetworkResponse<ImagesData> {
        val dog = diskDataSource.getBreedById(id)
        return when (val networkResult = networkDataSource.getAllUrlOfBreed(dog.breedName, dog.subBreedName)) {
            is NetworkResult -> {
                diskDataSource.insertImagesObject(
                    networkResult.result.message.map {
                        RoomImageData(
                            url = it,
                            breedId = id
                        )
                    }
                )
                networkResult
            }
            else -> networkResult
        }
    }

    fun getImagesByBreedId(id: String): List<RoomImageData> {
        return diskDataSource.getImagesById(id)
    }

    fun updateBreedFavoriteById(id: String, newIsFavorite: Boolean) {
        val breedItem = diskDataSource.getBreedById(id)
        diskDataSource.updateBreed(
            RoomBreedData(
                id = breedItem.id,
                breedName = breedItem.breedName,
                subBreedName = breedItem.subBreedName,
                isFavorite = newIsFavorite
            )
        )
    }
}

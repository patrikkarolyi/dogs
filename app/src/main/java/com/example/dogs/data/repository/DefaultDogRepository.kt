package com.example.dogs.data.repository

import com.example.dogs.data.disk.DiskDataSource
import com.example.dogs.data.disk.model.RoomBreedData
import com.example.dogs.data.disk.model.fullName
import com.example.dogs.data.network.NetworkDataSource
import com.example.dogs.data.network.model.AllBreedData
import com.example.dogs.data.network.model.NetworkResponse
import com.example.dogs.data.network.model.NetworkResult
import com.example.dogs.data.presentation.DogPresentationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultDogRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val diskDataSource: DiskDataSource,
) : DogRepository {

    override fun observeAllBreeds(): Flow<List<DogPresentationModel>> {
        return diskDataSource.observeAllBreeds()
            .map { list ->
                list.map { roomItem ->
                    DogPresentationModel(
                        breedId = roomItem.id,
                        fullName = roomItem.fullName(),
                    )
                }
            }
    }

    override suspend fun getAllBreeds(): List<DogPresentationModel> {
        return diskDataSource.getAllBreeds().map { roomItem ->
            DogPresentationModel(
                breedId = roomItem.id,
                fullName = roomItem.fullName(),
            )
        }
    }

    override suspend fun downloadAllBreeds(): NetworkResponse<AllBreedData> =
        withContext(Dispatchers.IO) {
            when (val networkResponse = networkDataSource.getAllBreeds()) {
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
            subBreedName = subBreed
        )
    }
}
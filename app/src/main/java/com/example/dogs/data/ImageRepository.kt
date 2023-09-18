package com.example.dogs.data

import com.example.dogs.data.disk.DiskDataSource
import com.example.dogs.data.disk.model.RoomImageData
import com.example.dogs.network.NetworkDataSource
import com.example.dogs.network.model.ImagesData
import com.example.dogs.network.model.NetworkResponse
import com.example.dogs.network.model.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ImageRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val diskDataSource: DiskDataSource,
) {

    suspend fun downloadImagesByBreedId(id: String): NetworkResponse<ImagesData> {
        val dog = diskDataSource.getBreedById(id)
        return when (val networkResult = networkDataSource.getAllUrlOfBreed(dog.breedName, dog.subBreedName)) {
            is NetworkResult -> {
                diskDataSource.insertImagesObject(
                    networkResult.result.message.map {
                        RoomImageData(
                            url = it,
                            breedId = id,
                            isFavorite = false
                        )
                    }
                )
                networkResult
            }
            else -> networkResult
        }
    }

    fun getImagesByBreedId(breedId: String): List<RoomImageData> {
        return diskDataSource.getImagesByBreedId(breedId)
    }

    fun observeAllFavoriteImages(): Flow<List<RoomImageData>> {
        return diskDataSource.getAllFavoriteImages()
    }

    fun updateImageFavoriteById(id: String, newIsFavorite: Boolean) {
        val imageItem = diskDataSource.getImageById(id)
        diskDataSource.updateBreed(
            RoomImageData(
                url = imageItem.url,
                breedId = imageItem.breedId,
                isFavorite = newIsFavorite
            )
        )
    }
}
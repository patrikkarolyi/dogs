package com.example.dogs.data

import com.example.dogs.data.disk.DiskDataSource
import com.example.dogs.data.disk.model.RoomImageData
import com.example.dogs.data.disk.model.fullName
import com.example.dogs.data.network.NetworkDataSource
import com.example.dogs.data.network.model.ImagesData
import com.example.dogs.data.network.model.NetworkResponse
import com.example.dogs.data.network.model.NetworkResult
import com.example.dogs.data.presentation.ImagePresentationModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
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

    fun observeAllImages(): Flow<List<ImagePresentationModel>> {
        return combine(
            diskDataSource.getAllImages(),
            diskDataSource.getAllBreeds()
        ) { images, dogs ->
            images.map { roomItem ->
                ImagePresentationModel(
                    url = roomItem.url,
                    breedId = roomItem.breedId,
                    isFavorite = roomItem.isFavorite,
                    fullName = dogs
                        .first { dog -> roomItem.breedId == dog.id }
                        .fullName(),
                )
            }
        }
    }

    fun getImagesByBreedId(breedId: String): List<RoomImageData> {
        return diskDataSource.getImagesByBreedId(breedId)
    }

    fun observeAllFavoriteImages(): Flow<List<ImagePresentationModel>> {
        return combine(
            diskDataSource.getAllFavoriteImages(),
            diskDataSource.getAllBreeds()
        ) { images, dogs ->
            images.map { roomItem ->
                ImagePresentationModel(
                    url = roomItem.url,
                    breedId = roomItem.breedId,
                    isFavorite = roomItem.isFavorite,
                    fullName = dogs
                        .first { dog -> roomItem.breedId == dog.id }
                        .fullName(),
                )
            }
        }
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
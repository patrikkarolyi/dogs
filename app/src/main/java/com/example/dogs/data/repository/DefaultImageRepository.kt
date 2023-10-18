package com.example.dogs.data.repository

import com.example.dogs.data.disk.DiskDataSource
import com.example.dogs.data.disk.model.RoomImageData
import com.example.dogs.data.disk.model.fullName
import com.example.dogs.data.network.NetworkDataSource
import com.example.dogs.data.network.model.ImagesData
import com.example.dogs.data.network.model.NetworkResponse
import com.example.dogs.data.network.model.NetworkResult
import com.example.dogs.data.presentation.ImagePresentationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultImageRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val diskDataSource: DiskDataSource,
) : ImageRepository {

    override suspend fun downloadImagesByBreedId(id: String): NetworkResponse<ImagesData> =
        withContext(Dispatchers.IO) {
            val dog = diskDataSource.getBreedById(id)
            when (val networkResult =
                networkDataSource.getAllUrlOfBreed(dog.breedName, dog.subBreedName)) {
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

    override fun observeAllImages(): Flow<List<ImagePresentationModel>> {
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

    override fun observeAllFavoriteImages(): Flow<List<ImagePresentationModel>> {
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

    override suspend fun updateImageFavoriteByUrl(url: String, newIsFavorite: Boolean) =
        withContext(Dispatchers.IO) {
            val imageItem = diskDataSource.getImageById(url)
            diskDataSource.updateBreed(
                RoomImageData(
                    url = imageItem.url,
                    breedId = imageItem.breedId,
                    isFavorite = newIsFavorite
                )
            )
        }
}
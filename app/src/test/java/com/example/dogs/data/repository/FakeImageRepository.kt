package com.example.dogs.data.repository

import com.example.dogs.data.network.model.ImagesData
import com.example.dogs.data.network.model.NetworkIOError
import com.example.dogs.data.network.model.NetworkResponse
import com.example.dogs.data.presentation.ImagePresentationModel
import com.example.dogs.data.test_data.imageTestData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeImageRepository : ImageRepository {

    private val images = MutableStateFlow<List<ImagePresentationModel>>(emptyList())

    private val favoriteImages = images.map { list -> list.filter { item -> item.isFavorite } }

    override suspend fun downloadImagesByBreedId(id: String): NetworkResponse<ImagesData> {
        //Assuming there are already images downloaded
        images.emit(imageTestData)
        return NetworkIOError
    }

    override fun observeAllImages(): Flow<List<ImagePresentationModel>> {
        return images
    }

    override fun observeAllFavoriteImages(): Flow<List<ImagePresentationModel>> {
        return favoriteImages
    }

    override suspend fun getRandomImageByBreedId(breedId: String): ImagePresentationModel {
        //TODO
        return ImagePresentationModel(
            url = "",
            breedId = "",
            fullName = "",
            isFavorite = true
        )
    }

    override suspend fun updateImageFavoriteByUrl(url: String, newIsFavorite: Boolean) {
        images.emit(
            imageTestData.map { item ->
                if (item.url == url) item.copy(isFavorite = newIsFavorite) else item
            }
        )
    }
}
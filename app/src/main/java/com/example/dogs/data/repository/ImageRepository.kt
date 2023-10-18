package com.example.dogs.data.repository

import com.example.dogs.data.network.model.ImagesData
import com.example.dogs.data.network.model.NetworkResponse
import com.example.dogs.data.presentation.ImagePresentationModel
import kotlinx.coroutines.flow.Flow

interface ImageRepository {

    suspend fun downloadImagesByBreedId(id: String): NetworkResponse<ImagesData>
    fun observeAllImages(): Flow<List<ImagePresentationModel>>
    fun observeAllFavoriteImages(): Flow<List<ImagePresentationModel>>
    suspend fun updateImageFavoriteByUrl(url: String, newIsFavorite: Boolean)
}
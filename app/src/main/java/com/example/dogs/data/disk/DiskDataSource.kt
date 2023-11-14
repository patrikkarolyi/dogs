package com.example.dogs.data.disk

import com.example.dogs.data.disk.model.RoomBreedData
import com.example.dogs.data.disk.model.RoomImageData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DiskDataSource @Inject constructor(
    private val dogsDAO: DogDAO,
    private val imageDAO: ImageDAO
) {

    fun observeAllBreeds(): Flow<List<RoomBreedData>> {
        return dogsDAO.observeAllBreeds()
    }

    suspend fun getAllBreeds(): List<RoomBreedData> {
        return dogsDAO.getAllBreeds()
    }

    suspend fun getBreedById(id: String): RoomBreedData {
        return dogsDAO.getBreedById(id)
    }

    suspend fun insertBreeds(data: List<RoomBreedData>) {
        return dogsDAO.insertBreeds(data)
    }

    fun observeAllImages(): Flow<List<RoomImageData>> {
        return imageDAO.observeAllImages()
    }

    suspend fun getAllImages(): List<RoomImageData> {
        return imageDAO.getAllImages()
    }

    fun getAllFavoriteImages(): Flow<List<RoomImageData>> {
        return imageDAO.observeAllFavoriteImages()
    }

    suspend fun getImagesByBreedId(breedId: String): List<RoomImageData> {
        return imageDAO.getImagesByBreedId(breedId)
    }

    suspend fun getImageByUrl(id: String): RoomImageData {
        return imageDAO.getImageByUrl(id)
    }

    suspend fun insertImagesObject(images: List<RoomImageData>){
        return imageDAO.insertImages(images)
    }

    suspend fun updateBreed(data: RoomImageData) {
        return imageDAO.updateImage(data)
    }
}
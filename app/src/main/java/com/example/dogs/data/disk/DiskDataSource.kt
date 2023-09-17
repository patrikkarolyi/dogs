package com.example.dogs.data.disk

import com.example.dogs.data.disk.model.RoomBreedData
import com.example.dogs.data.disk.model.RoomImageData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DiskDataSource @Inject constructor(
    private val dogsDAO: DogDAO,
    private val imageDAO: ImageDAO
) {

    fun getAllBreeds(): Flow<List<RoomBreedData>> {
        return dogsDAO.getAllBreeds()
    }

    fun getAllFavoriteBreeds(): Flow<List<RoomBreedData>> {
        return dogsDAO.getAllFavoriteBreeds()
    }

    fun getBreedById(id: String): RoomBreedData {
        return dogsDAO.getBreedById(id)
    }

    fun insertBreeds(data: List<RoomBreedData>) {
        return dogsDAO.insertBreeds(data)
    }

    fun updateBreed(data: RoomBreedData) {
        return dogsDAO.updateBreed(data)
    }

    fun getAllFavoriteImages(): List<RoomImageData> {
        return imageDAO.getAllFavoriteImages()
    }

    fun getImagesByBreedId(breedId: String): List<RoomImageData> {
        return imageDAO.getImagesByBreedId(breedId)
    }

    fun getImageById(id: String): RoomImageData {
        return imageDAO.getImageById(id)
    }

    fun insertImagesObject(images: List<RoomImageData>){
        return imageDAO.insertImages(images)
    }

    fun updateBreed(data: RoomImageData) {
        return imageDAO.updateImage(data)
    }
}
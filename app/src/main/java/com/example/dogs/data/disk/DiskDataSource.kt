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

    fun getBreedById(id: String): RoomBreedData {
        return dogsDAO.getBreedById(id)
    }

    fun insertBreeds(data: List<RoomBreedData>) {
        return dogsDAO.insertBreeds(data)
    }

    fun getAllImages(): Flow<List<RoomImageData>> {
        return imageDAO.getAllImages()
    }

    fun getAllFavoriteImages(): Flow<List<RoomImageData>> {
        return imageDAO.getAllFavoriteImages()
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
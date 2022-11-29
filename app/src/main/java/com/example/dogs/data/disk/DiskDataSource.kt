package com.example.dogs.data.disk

import com.example.dogs.data.disk.model.RoomBreedData
import com.example.dogs.data.disk.model.RoomImageData
import javax.inject.Inject

class DiskDataSource @Inject constructor(
    private val dogsDAO: DogDAO,
    private val imageDAO: ImageDAO
) {

    fun getAllBreeds(): List<RoomBreedData> {
        return dogsDAO.getAllBreeds()
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

    fun getImagesById(id: String): List<RoomImageData> {
        return imageDAO.getImagesById(id)
    }

    fun insertImagesObject(images: List<RoomImageData>){
        return imageDAO.insertImagesObject(images)
    }
}
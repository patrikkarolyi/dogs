package com.example.dogs.data.disk

import androidx.room.*
import com.example.dogs.data.disk.model.RoomImageData
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDAO {

    @Query("SELECT * FROM images")
    fun getAllImages(): Flow<List<RoomImageData>>

    @Query("SELECT * FROM images WHERE isFavorite=1")
    fun getAllFavoriteImages(): Flow<List<RoomImageData>>

    @Query("SELECT * FROM images WHERE breedId=:breedId")
    fun getImagesByBreedId(breedId: String):  List<RoomImageData>

    @Query("SELECT * FROM images WHERE url=:url")
    fun getImageById(url: String): RoomImageData

    @Upsert
    fun insertImages(images: List<RoomImageData>)

    @Update()
    fun updateImage(breed: RoomImageData)
}
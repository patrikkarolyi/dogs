package com.example.dogs.data.disk

import androidx.room.*
import com.example.dogs.data.disk.model.RoomImageData

@Dao
interface ImageDAO {

    @Transaction
    @Query("SELECT * FROM images WHERE isFavorite=1")
    fun getAllFavoriteImages(): List<RoomImageData>

    @Transaction
    @Query("SELECT * FROM images WHERE breedId=:breedId")
    fun getImagesByBreedId(breedId: String): List<RoomImageData>

    @Transaction
    @Query("SELECT * FROM images WHERE url=:url")
    fun getImageById(url: String): RoomImageData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImages(images: List<RoomImageData>)

    @Update()
    fun updateImage(breed: RoomImageData)
}
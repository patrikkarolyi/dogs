package com.example.dogs.data.disk

import androidx.room.*
import com.example.dogs.data.disk.model.RoomImageData
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDAO {

    @Query("SELECT * FROM images")
    fun observeAllImages(): Flow<List<RoomImageData>>

    @Query("SELECT * FROM images")
    suspend fun getAllImages(): List<RoomImageData>

    @Query("SELECT * FROM images WHERE isFavorite=1")
    fun observeAllFavoriteImages(): Flow<List<RoomImageData>>

    @Query("SELECT * FROM images WHERE breedId=:breedId")
    suspend fun getImagesByBreedId(breedId: String):  List<RoomImageData>

    @Query("SELECT * FROM images WHERE url=:url")
    suspend fun getImageByUrl(url: String): RoomImageData

    @Upsert
    suspend fun insertImages(images: List<RoomImageData>)

    @Update()
    suspend fun updateImage(breed: RoomImageData)
}
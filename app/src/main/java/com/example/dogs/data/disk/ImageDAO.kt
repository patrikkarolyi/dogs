package com.example.dogs.data.disk

import androidx.room.*
import com.example.dogs.data.disk.model.RoomImageData

@Dao
interface ImageDAO {

    @Transaction
    @Query("SELECT * FROM images WHERE breedId=:breedId")
    fun getImagesById(breedId: String): List<RoomImageData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImagesObject(images: List<RoomImageData>)
}
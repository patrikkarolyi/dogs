package com.example.dogs.data.disk

import androidx.room.*
import com.example.dogs.data.disk.model.RoomBreedData

@Dao
interface DogDAO {

    @Transaction
    @Query("SELECT * FROM breeds")
    fun getAllBreeds(): List<RoomBreedData>

    @Transaction
    @Query("SELECT * FROM breeds WHERE isFavorite=1")
    fun getAllFavoriteBreeds(): List<RoomBreedData>

    @Transaction
    @Query("SELECT * FROM breeds WHERE id=:id")
    fun getBreedById(id: String): RoomBreedData

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertBreeds(breeds: List<RoomBreedData>)

    @Update()
    fun updateBreed(breed: RoomBreedData)
}
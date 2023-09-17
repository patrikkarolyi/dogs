package com.example.dogs.data.disk

import androidx.room.*
import com.example.dogs.data.disk.model.RoomBreedData
import kotlinx.coroutines.flow.Flow

@Dao
interface DogDAO {

    @Transaction
    @Query("SELECT * FROM breeds")
    fun getAllBreeds(): Flow<List<RoomBreedData>>

    @Transaction
    @Query("SELECT * FROM breeds WHERE isFavorite=1")
    fun getAllFavoriteBreeds(): Flow<List<RoomBreedData>>

    @Transaction
    @Query("SELECT * FROM breeds WHERE id=:id")
    fun getBreedById(id: String): RoomBreedData

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertBreeds(breeds: List<RoomBreedData>)

    @Update()
    fun updateBreed(breed: RoomBreedData)
}
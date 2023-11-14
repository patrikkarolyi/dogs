package com.example.dogs.data.disk

import androidx.room.*
import com.example.dogs.data.disk.model.RoomBreedData
import kotlinx.coroutines.flow.Flow

@Dao
interface DogDAO {

    @Query("SELECT * FROM breeds")
    fun observeAllBreeds(): Flow<List<RoomBreedData>>

    @Query("SELECT * FROM breeds")
    suspend fun getAllBreeds(): List<RoomBreedData>

    @Query("SELECT * FROM breeds WHERE id=:id")
    suspend fun getBreedById(id: String): RoomBreedData

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBreeds(breeds: List<RoomBreedData>)
}
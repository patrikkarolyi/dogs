package com.example.dogs.data.disk

import androidx.room.*
import com.example.dogs.data.disk.model.RoomBreedData
import kotlinx.coroutines.flow.Flow

@Dao
interface DogDAO {

    @Query("SELECT * FROM breeds")
    fun getAllBreeds(): Flow<List<RoomBreedData>>

    @Query("SELECT * FROM breeds WHERE id=:id")
    fun getBreedById(id: String): RoomBreedData

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertBreeds(breeds: List<RoomBreedData>)
}
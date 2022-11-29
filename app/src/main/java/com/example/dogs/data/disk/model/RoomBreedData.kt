package com.example.dogs.data.disk.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Breeds")
data class RoomBreedData(
    @PrimaryKey
    val id: String,
    val breedName: String,
    val subBreedName: String,
    val isFavorite: Boolean
)
package com.example.dogs.data.disk.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Images")
data class RoomImageData (
    @PrimaryKey
    val url: String,
    val breedId: String
)
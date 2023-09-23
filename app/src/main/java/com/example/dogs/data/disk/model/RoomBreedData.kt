package com.example.dogs.data.disk.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dogs.util.capFirst

@Entity(tableName = "Breeds")
data class RoomBreedData(
    @PrimaryKey
    val id: String,
    val breedName: String,
    val subBreedName: String,
)

fun RoomBreedData.fullName() = "${this.subBreedName.capFirst()} ${this.breedName.capFirst()}".trim()
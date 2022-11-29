package com.example.dogs.data.disk

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dogs.data.disk.model.RoomBreedData
import com.example.dogs.data.disk.model.RoomImageData

@Database(entities = [RoomBreedData::class, RoomImageData::class], version = 1)
abstract class DogDatabase : RoomDatabase() {
    abstract fun dogDao(): DogDAO
    abstract fun imageDao(): ImageDAO
}
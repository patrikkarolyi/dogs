package com.example.dogs.di

import android.content.Context
import androidx.room.Room
import com.example.dogs.data.disk.DogDAO
import com.example.dogs.data.disk.DogDatabase
import com.example.dogs.data.disk.ImageDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DiskModule {

    companion object {
        private const val DB_NAME = "dog-db"
    }

    @Provides
    fun provideDogDao(db: DogDatabase): DogDAO = db.dogDao()

    @Provides
    fun provideImageDao(db: DogDatabase): ImageDAO = db.imageDao()

    @Provides
    fun provideDogDatabase(context: Context): DogDatabase {
        return Room.databaseBuilder(context, DogDatabase::class.java, DB_NAME).build()
    }
}
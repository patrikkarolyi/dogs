package com.example.dogs.di

import com.example.dogs.data.repository.DefaultDogRepository
import com.example.dogs.data.repository.DefaultImageRepository
import com.example.dogs.data.repository.DogRepository
import com.example.dogs.data.repository.ImageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideDogRepository(dogRepository: DefaultDogRepository): DogRepository

    @Binds
    abstract fun provideImageRepository(imageRepository: DefaultImageRepository): ImageRepository
}
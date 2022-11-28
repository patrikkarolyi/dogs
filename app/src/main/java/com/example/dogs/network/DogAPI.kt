package com.example.dogs.network

import com.example.dogs.network.model.AllBreedData
import com.example.dogs.network.model.ImageData
import com.example.dogs.network.model.ImagesData
import retrofit2.http.GET
import retrofit2.http.Path

interface DogAPI {

    companion object {
        const val baseUrl = "https://dog.ceo/api/"
    }

    @GET("breeds/list/all")
    suspend fun getAllBreeds(): AllBreedData

    @GET("breed/{breed}/images/random")
    suspend fun getRandomUrlOfBreed(
        @Path("breed") breed: String
    ): ImageData

    @GET("breed/{breed}/{subbreed}/images/random")
    suspend fun getRandomUrlOfBreed(
        @Path("breed") breed: String,
        @Path("subbreed") subbreed: String,
    ): ImageData

    @GET("breed/{breed}/images")
    suspend fun getAllUrlOfBreed(
        @Path("breed") breed: String
    ): ImagesData

    @GET("breed/{breed}/{subbreed}/images")
    suspend fun getAllUrlOfBreed(
        @Path("breed") breed: String,
        @Path("subbreed") subbreed: String,
    ): ImagesData

}
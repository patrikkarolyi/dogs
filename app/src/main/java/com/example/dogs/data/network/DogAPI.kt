package com.example.dogs.data.network

import com.example.dogs.data.network.model.AllBreedData
import com.example.dogs.data.network.model.ImagesData
import retrofit2.http.GET
import retrofit2.http.Path

interface DogAPI {

    companion object {
        const val baseUrl = "https://dog.ceo/api/"
    }

    @GET("breeds/list/all")
    suspend fun getAllBreeds(): AllBreedData

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
package com.example.dogs.data.network.model

import com.squareup.moshi.Json

data class AllBreedData(
    @Json(name = "message") val message: Map<String, List<String>>,
    @Json(name = "status") val status: String
)
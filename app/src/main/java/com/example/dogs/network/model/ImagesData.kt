package com.example.dogs.network.model

import com.squareup.moshi.Json

data class ImagesData(
    @Json(name = "message") val message: List<String>,
    @Json(name = "status") val status: String
)
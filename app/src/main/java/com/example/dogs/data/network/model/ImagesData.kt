package com.example.dogs.data.network.model

import com.squareup.moshi.Json

data class ImagesData(
    @Json(name = "message") val message: List<String>,
    @Json(name = "status") val status: String
)
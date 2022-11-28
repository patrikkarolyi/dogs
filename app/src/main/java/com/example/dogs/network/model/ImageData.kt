package com.example.dogs.network.model

import com.squareup.moshi.Json

data class ImageData(
    @Json(name = "message") val message: String,
    @Json(name = "status") val status: String
)
package com.example.dogs.data.test_data

import com.example.dogs.data.presentation.ImagePresentationModel

val imageTestData = listOf(
    ImagePresentationModel(
        url = "url1",
        breedId = "akita",
        fullName = "akita",
        isFavorite = false
    ),
    ImagePresentationModel(
        url = "url2",
        breedId = "bulldogfrench",
        fullName = "French Bulldog",
        isFavorite = false
    ),
    ImagePresentationModel(
        url = "url3",
        breedId = "dalmatian",
        fullName = "Dalmatian",
        isFavorite = false
    ),
    ImagePresentationModel(
        url = "url4",
        breedId = "komondor",
        fullName = "Komondor",
        isFavorite = true
    ),
    ImagePresentationModel(
        url = "url5",
        breedId = "vizsla",
        fullName = "Vizsla",
        isFavorite = true
    )
)
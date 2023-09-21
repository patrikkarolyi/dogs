package com.example.dogs.ui.favorite

import com.example.dogs.data.presentation.ImagePresentationModel

data class FavoriteViewContent(
    val result: List<ImagePresentationModel> = emptyList()
)
package com.example.dogs.ui.details

import com.example.dogs.data.presentation.ImagePresentationModel

data class DetailsViewContent(
    val result: List<ImagePresentationModel> = emptyList(),
    val isRefreshing: Boolean = true
)
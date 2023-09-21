package com.example.dogs.ui.list

import com.example.dogs.data.presentation.DogPresentationModel

data class ListViewContent(
    val result: List<DogPresentationModel> = emptyList()
)
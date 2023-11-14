package com.example.dogs.ui.game

import com.example.dogs.data.presentation.DogPresentationModel
import com.example.dogs.data.presentation.ImagePresentationModel


sealed class GameViewState{
    data object Loading: GameViewState()

    data class GameViewContent(
        val image: ImagePresentationModel? = null,
        val dogNames: List<DogPresentationModel> = emptyList()
    ): GameViewState()
}

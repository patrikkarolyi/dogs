package com.example.dogs.ui.fav_dog

import com.example.dogs.data.disk.model.RoomBreedData

sealed class FavDogViewState {
    object Initial : FavDogViewState()
    data class Content(val result: List<RoomBreedData>) : FavDogViewState()
}
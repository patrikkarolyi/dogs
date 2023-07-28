package com.example.dogs.ui.favoriteDogs

import com.example.dogs.data.disk.model.RoomBreedData

sealed class FavoriteDogsViewState {
    object Initial : FavoriteDogsViewState()
    data class Content(val result: List<RoomBreedData>) : FavoriteDogsViewState()
}
package com.example.dogs.ui.favorite

import com.example.dogs.data.disk.model.RoomImageData

sealed class FavoriteViewState{
    object Initial : FavoriteViewState()
    class Content(val result: List<RoomImageData>) : FavoriteViewState()
}
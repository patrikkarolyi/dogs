package com.example.dogs.ui.favoriteImages

import com.example.dogs.data.disk.model.RoomImageData

sealed class FavoriteImagesViewState{
    object Initial : FavoriteImagesViewState()
    class Content(val result: List<RoomImageData>) : FavoriteImagesViewState()
}
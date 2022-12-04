package com.example.dogs.ui.favoriteImages

import com.example.dogs.data.disk.model.RoomImageData

sealed class FavoriteImagesViewState

object Initial : FavoriteImagesViewState()

object Refreshing : FavoriteImagesViewState()

class NetworkError(val message: String) : FavoriteImagesViewState()

class Content(val result: List<RoomImageData>) : FavoriteImagesViewState()
package com.example.dogs.ui.favorite

import com.example.dogs.data.disk.model.RoomBreedData

sealed class FavoriteViewState

object Initial : FavoriteViewState()

object Refreshing : FavoriteViewState()

class NetworkError(val message: String) : FavoriteViewState()

class Content(val result: List<RoomBreedData>) : FavoriteViewState()
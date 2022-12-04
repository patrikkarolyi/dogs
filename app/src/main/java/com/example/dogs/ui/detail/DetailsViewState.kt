package com.example.dogs.ui.detail

import com.example.dogs.data.disk.model.RoomImageData

sealed class DetailsViewState

object Initial : DetailsViewState()

object Refreshing : DetailsViewState()

class NetworkError(val message: String) : DetailsViewState()

class Content(val result: List<RoomImageData>) : DetailsViewState()
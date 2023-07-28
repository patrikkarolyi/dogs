package com.example.dogs.ui.detail

import com.example.dogs.data.disk.model.RoomImageData

sealed class DetailsViewState{
    object Initial : DetailsViewState()
    data class Content(val result: List<RoomImageData>, val breedId: String) : DetailsViewState()
}
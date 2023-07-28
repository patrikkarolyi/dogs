package com.example.dogs.ui.detail

import com.example.dogs.data.disk.model.RoomImageData

sealed class DetailViewState{
    object Initial : DetailViewState()
    data class Content(val result: List<RoomImageData>, val breedId: String) : DetailViewState()
}
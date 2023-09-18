package com.example.dogs.ui.details

import com.example.dogs.data.disk.model.RoomImageData

sealed class DetailViewState{
    object Initial : DetailViewState()
    data class Content(val result: List<RoomImageData>) : DetailViewState()
}
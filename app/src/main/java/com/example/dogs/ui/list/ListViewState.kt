package com.example.dogs.ui.list

import com.example.dogs.data.disk.model.RoomBreedData

sealed class ListViewState {
    object Initial : ListViewState()
    data class Content(val result: List<RoomBreedData>) : ListViewState()
}
package com.example.dogs.ui.list

import com.example.dogs.data.disk.model.RoomBreedData

sealed class ListViewState{
    object Initial : ListViewState()

    object Refreshing : ListViewState()

    data class NetworkError(val message: String) : ListViewState()

    data class Content(val result: List<RoomBreedData>, val clearEditText: Boolean = true ) : ListViewState()
}
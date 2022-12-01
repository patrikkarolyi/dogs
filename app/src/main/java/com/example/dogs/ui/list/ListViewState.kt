package com.example.dogs.ui.list

import com.example.dogs.data.disk.model.RoomBreedData

sealed class ListViewState

object Initial : ListViewState()

object Refreshing : ListViewState()

class NetworkError(val message: String) : ListViewState()

class Content(val result: List<RoomBreedData>) : ListViewState()
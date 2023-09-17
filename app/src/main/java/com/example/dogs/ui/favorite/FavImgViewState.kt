package com.example.dogs.ui.favorite

import com.example.dogs.data.disk.model.RoomImageData

sealed class FavImgViewState{
    object Initial : FavImgViewState()
    class Content(val result: List<RoomImageData>) : FavImgViewState()
}
package com.example.dogs.ui.fav_img

import com.example.dogs.data.disk.model.RoomImageData

sealed class FavImgViewState{
    object Initial : FavImgViewState()
    class Content(val result: List<RoomImageData>) : FavImgViewState()
}
package com.example.dogs.ui.favorite

import com.example.dogs.ui.common.model.ImageViewState


sealed class FavoriteViewState{
    object Initial : FavoriteViewState()
    class Content(val result: List<ImageViewState>) : FavoriteViewState()
}
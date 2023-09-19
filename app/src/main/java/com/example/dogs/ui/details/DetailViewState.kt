package com.example.dogs.ui.details

import com.example.dogs.ui.common.model.ImageViewState

sealed class DetailViewState{
    object Initial : DetailViewState()
    data class Content(val result: List<ImageViewState>) : DetailViewState()
}
package com.example.dogs.ui.detail

sealed class DetailsViewState

object Initial : DetailsViewState()

object Refreshing : DetailsViewState()

class NetworkError(val message: String) : DetailsViewState()

class Content(val result: List<String>) : DetailsViewState()
package com.example.dogs.ui.detail

import androidx.lifecycle.ViewModel
import com.example.dogs.data.DogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val dataSource: DogRepository,
) : ViewModel() {}
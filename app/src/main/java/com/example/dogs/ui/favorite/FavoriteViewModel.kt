package com.example.dogs.ui.favorite

import androidx.lifecycle.ViewModel
import com.example.dogs.data.DogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val dataSource: DogRepository,
) : ViewModel() {}
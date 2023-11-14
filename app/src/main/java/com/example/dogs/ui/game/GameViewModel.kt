package com.example.dogs.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogs.data.network.model.NetworkResult
import com.example.dogs.data.network.model.translateNetworkResponse
import com.example.dogs.data.presentation.NetworkErrorPresentationModel
import com.example.dogs.data.repository.DogRepository
import com.example.dogs.data.repository.ImageRepository
import com.example.dogs.ui.game.GameViewState.GameViewContent
import com.example.dogs.ui.game.GameViewState.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val imageRepository: ImageRepository,
    private val dogRepository: DogRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<GameViewState>(Loading)
    val uiState: StateFlow<GameViewState> = _uiState

    private val numberOfAnswers = 4
    private var lastGoodAnswer = ""

    var errorMessage = MutableSharedFlow<NetworkErrorPresentationModel>()
        private set

    init {
        resetQuestion()
    }

    fun resetQuestion() {
        viewModelScope.launch {
            _uiState.update { Loading }

            val dogs = dogRepository.getAllBreeds()
            val dog = dogs.random()
            lastGoodAnswer = dog.breedId

            imageRepository.downloadImagesByBreedId(dog.breedId).let { result ->
                if (result !is NetworkResult) {
                    errorMessage.emit(result.translateNetworkResponse())
                    return@launch
                }
            }

            val image = imageRepository.getRandomImageByBreedId(dog.breedId)
            val randomDogs = mutableSetOf(dog)
            while (randomDogs.size < numberOfAnswers) {
                randomDogs.add(dogs.random())
            }

            _uiState.update {
                GameViewContent(
                    image = image,
                    dogNames = randomDogs.shuffled()
                )
            }
        }
    }

    fun submitAnswer(breedId: String): Boolean {
        println("$lastGoodAnswer - $breedId")
        return lastGoodAnswer == breedId
    }
}
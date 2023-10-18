package com.example.dogs.ui.details

import androidx.lifecycle.SavedStateHandle
import com.example.dogs.data.repository.FakeImageRepository
import com.example.dogs.data.test_data.imageTestData
import com.example.dogs.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val imageRepository = FakeImageRepository()
    private val savedStateHandle = SavedStateHandle()
    private lateinit var viewModel: DetailsViewModel

    @Before
    fun setup() {
        viewModel = DetailsViewModel(
            imageRepository = imageRepository,
            savedStateHandle = savedStateHandle,
        )
    }

    @Test
    fun `Given the test data set Then there is at least one image shown`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        val currentBreedId = imageTestData.first().breedId

        viewModel.refreshImageUrls(currentBreedId)

        Assert.assertTrue(viewModel.uiState.value.result.isNotEmpty())
        collectJob.cancel()
    }

    @Test
    fun `Only one dog's images are shown`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        val currentBreedId = imageTestData.first().breedId

        viewModel.refreshImageUrls(currentBreedId)

        Assert.assertTrue(viewModel.uiState.value.result.all { it.breedId == currentBreedId })
        collectJob.cancel()
    }
}
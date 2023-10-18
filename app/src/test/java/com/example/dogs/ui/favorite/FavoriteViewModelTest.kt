package com.example.dogs.ui.favorite

import androidx.lifecycle.SavedStateHandle
import com.example.dogs.data.repository.FakeImageRepository
import com.example.dogs.data.test_data.imageTestData
import com.example.dogs.ui.details.DetailsViewModel
import com.example.dogs.util.MainDispatcherRule
import junit.framework.TestCase.assertEquals
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
class FavoriteViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val imageRepository = FakeImageRepository()
    private val savedStateHandle = SavedStateHandle()
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var helperViewModel: DetailsViewModel

    @Before
    fun setup() {
        viewModel = FavoriteViewModel(
            imageRepository = imageRepository,
            savedStateHandle = savedStateHandle,
        )
        helperViewModel = DetailsViewModel(
            imageRepository = imageRepository,
            savedStateHandle = savedStateHandle,
        )
        helperViewModel.refreshImageUrls()
    }

    @Test
    fun `Only favorite items are shown`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        assertEquals(
            imageTestData.filter { it.isFavorite },
            viewModel.uiState.value.result
        )
        collectJob.cancel()
    }

    @Test
    fun `Given favorite images When updating one isFavorite property to false Then it is no more shown`() =
        runTest {
            val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
            val urlToInteract = imageTestData.first { it.isFavorite }.url

            viewModel.updateImageFavoriteByUrl(urlToInteract, false)

            Assert.assertTrue(
                viewModel.uiState.value.result.isNotEmpty()
                        &&
                        viewModel.uiState.value.result.none { it.url == urlToInteract }
            )
            collectJob.cancel()
        }

    @Test
    fun `Given favorite images When update filter Then only matching dogs are shown`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        val urlToInteract = imageTestData.first { it.isFavorite }.breedId

        viewModel.updateFilters(urlToInteract)

        Assert.assertTrue(
            viewModel.uiState.value.result.isNotEmpty()
                    &&
                    viewModel.uiState.value.result.all { it.breedId.contains(urlToInteract) }
        )
        collectJob.cancel()
    }
}
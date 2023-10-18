package com.example.dogs.ui.list

import androidx.lifecycle.SavedStateHandle
import com.example.dogs.data.repository.FakeDogRepository
import com.example.dogs.data.test_data.dogTestData
import com.example.dogs.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val dogRepository = FakeDogRepository()
    private val savedStateHandle = SavedStateHandle()
    private lateinit var viewModel: ListViewModel

    @Before
    fun setup() {
        viewModel = ListViewModel(
            dogRepository = dogRepository,
            savedStateHandle = savedStateHandle,
        )
    }

    @Test
    fun `When initialized Then it is refreshing`() = runTest {
        Assert.assertTrue(viewModel.uiState.value.isRefreshing)
    }

    @Test
    fun `When refreshed Then it is not refreshing`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        viewModel.refreshAllBreeds()
        Assert.assertFalse(viewModel.uiState.value.isRefreshing)
        collectJob.cancel()
    }

    @Test
    fun `When update filter Then only matching dogs are shown`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        val userInputFilter = dogTestData.first().breedId

        viewModel.refreshAllBreeds()
        viewModel.updateFilters(userInputFilter)

        assertEquals(
            dogTestData.filter { it.breedId.contains(userInputFilter) },
            viewModel.uiState.value.result
        )
        collectJob.cancel()
    }
}
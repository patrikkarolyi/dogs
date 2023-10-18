package com.example.dogs.ui.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.navigation.compose.rememberNavController
import com.example.dogs.data.test_data.dogTestData
import com.example.dogs.util.TestTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ListScreenTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun whenSwipeDown_thenTriggerRefresh() {
        var isRefreshed = false

        composeTestRule.setContent {
            val navController = rememberNavController()
            ListScreen(
                navController = navController,
                error = null,
                newItems = emptyList(),
                isRefreshing = false,
                onRefreshTriggered = { isRefreshed = true },
                onSearchTriggered = {},
            )
        }

        composeTestRule.onNodeWithTag(TestTags.EMPTY_CONTENT).performTouchInput { swipeDown() }
        assert(isRefreshed)
    }

    @Test
    fun whenNoContent_thenShowEmptyContent() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            ListScreen(
                navController = navController,
                error = null,
                newItems = emptyList(),
                isRefreshing = false,
                onRefreshTriggered = {},
                onSearchTriggered = {},
            )
        }

        composeTestRule.onNodeWithTag(TestTags.EMPTY_CONTENT).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.LIST_CONTENT).assertDoesNotExist()
    }

    @Test
    fun whenThereIsContent_thenShowDogListContent() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            ListScreen(
                navController = navController,
                error = null,
                newItems = dogTestData,
                isRefreshing = false,
                onRefreshTriggered = {},
                onSearchTriggered = {},
            )
        }

        composeTestRule.onNodeWithTag(TestTags.LIST_CONTENT).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.EMPTY_CONTENT).assertDoesNotExist()
    }
}
package com.example.dogs.ui.details

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.example.dogs.data.test_data.imageTestData
import com.example.dogs.util.TestTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class DetailsScreenTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun whenItemFavoriteClicked_thenCallbackFunctionIsInvoked() {
        var isFavoriteClicked = false

        composeTestRule.setContent {
            val navController = rememberNavController()
            DetailsScreen(
                navController = navController,
                error = null,
                title = "",
                newItems = imageTestData,
                isRefreshing = false,
                onRefreshTriggered = {},
                onItemFavoriteClicked = { _, _ -> isFavoriteClicked = true }
            )
        }

        composeTestRule.onAllNodesWithContentDescription("Favorites").onFirst().performClick()
        assert(isFavoriteClicked)
    }

    @Test
    fun whenThereIsContent_thenShowImageListContent() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            DetailsScreen(
                navController = navController,
                error = null,
                title = "",
                newItems = imageTestData,
                isRefreshing = false,
                onRefreshTriggered = {},
                onItemFavoriteClicked = { _, _ -> }
            )
        }

        composeTestRule.onNodeWithTag(TestTags.IMAGE_CONTENT).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.EMPTY_CONTENT).assertDoesNotExist()
    }
}
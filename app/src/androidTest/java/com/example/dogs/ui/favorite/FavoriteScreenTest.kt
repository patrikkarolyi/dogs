package com.example.dogs.ui.favorite

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.example.dogs.util.TestTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class FavoriteScreenTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun whenSearchBarIsClicked_thenItGetsFocus() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            FavoriteScreen(
                navController = navController,
                newItems = emptyList(),
                onSearchTriggered = {},
                onItemFavoriteClicked = { _, _ -> }
            )
        }
        composeTestRule.onNodeWithTag(TestTags.SEARCH_BAR).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.SEARCH_BAR).performClick()
        composeTestRule.onNodeWithTag(TestTags.SEARCH_BAR).assertIsFocused()
    }

    @Test
    fun whenSearchBarIsFilledAndDoneAction_thenOnSearchTriggered() {
        val userInput = "hello world!"
        var resultText = ""
        composeTestRule.setContent {
            val navController = rememberNavController()
            FavoriteScreen(
                navController = navController,
                newItems = emptyList(),
                onSearchTriggered = { text -> resultText = text },
                onItemFavoriteClicked = { _, _ -> }
            )
        }
        composeTestRule.onNodeWithTag(TestTags.SEARCH_BAR).performTextInput(userInput)
        composeTestRule.onNodeWithTag(TestTags.SEARCH_BAR).assert(hasText(userInput))
        composeTestRule.onNodeWithTag(TestTags.SEARCH_BAR).performImeAction()
        assert(resultText == userInput)
    }
}
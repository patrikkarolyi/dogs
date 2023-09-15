package com.example.dogs.ui.fav_dog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dogs.navigation.Screen
import com.example.dogs.ui.custom_view.ListContent
import com.example.dogs.ui.custom_view.MySearchToolbar
import com.example.dogs.ui.fav_dog.FavDogViewState.Content
import com.example.dogs.ui.fav_dog.FavDogViewState.Initial

@Composable
fun FavDogScreen(
    viewModel: FavDogViewModel = viewModel(),
    navController: NavController,
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    var text by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getAllFavoriteBreeds()
    }

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colors.background),
        scaffoldState = scaffoldState,
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(
                        tint = MaterialTheme.colors.secondary,
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                MySearchToolbar(
                    modifier = Modifier.weight(1f),
                    onSearchQueryChanged = { text = it },
                    searchQuery = text,
                    onSearchTriggered = { viewModel.updateFilters(it) }
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
        ) {
            when (val state = viewModel.uiState) {
                is Content -> ListContent(
                    state.result,
                    { id ->
                        navController.navigate(Screen.DetailScreen.withArgs(id))
                    },
                    viewModel::updateBreedFavoriteById
                )
                Initial -> {}
            }
        }
    }
}
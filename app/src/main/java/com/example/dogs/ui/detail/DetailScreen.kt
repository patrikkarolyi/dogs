package com.example.dogs.ui.detail

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
import androidx.compose.material.SnackbarResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dogs.ui.custom_view.DetailContent
import com.example.dogs.ui.detail.DetailViewState.Content
import com.example.dogs.ui.detail.DetailViewState.Initial
import kotlinx.coroutines.CoroutineScope


@Composable
fun DetailScreen(
    viewModel: DetailViewModel = viewModel(),
    navController: NavController,
    breedId: String
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    if (breedId.isNotBlank()) {
        LaunchedEffect(coroutineScope) {
            viewModel.getImageUrls(breedId)
            viewModel.errorMessage.collect { message ->
                val snackbarResut = scaffoldState.snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = "Retry",
                )
                when (snackbarResut) {
                    SnackbarResult.Dismissed -> {}
                    SnackbarResult.ActionPerformed -> {
                        viewModel.refreshImageUrls(breedId)
                    }
                }
            }
        }
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
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        tint = MaterialTheme.colors.secondary,
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
        ) {
            when (val state = viewModel.uiState) {
                is Content -> DetailContent(
                    newItems = state.result,
                    onItemFavoriteClicked = { url, isFavorite ->
                        viewModel.updateImageFavoriteById(breedId, url, isFavorite)
                    }
                )
                Initial -> {}
            }
        }
    }
}
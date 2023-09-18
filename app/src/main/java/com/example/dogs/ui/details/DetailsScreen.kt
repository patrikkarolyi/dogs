package com.example.dogs.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dogs.ui.common.DetailContent
import com.example.dogs.ui.details.DetailViewState.Content
import com.example.dogs.ui.details.DetailViewState.Initial
import kotlinx.coroutines.CoroutineScope


@Composable
fun DetailScreen(
    viewModel: DetailsViewModel = viewModel(),
    navController: NavController,
    breedId: String
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val viewState by viewModel.uiState.collectAsState()
    val currentBreedId by viewModel.currentBreedId.collectAsState()

    if (breedId.isNotBlank()) {
        LaunchedEffect(coroutineScope) {
            viewModel.refreshImageUrls(breedId = breedId)
            viewModel.errorMessage.collect { message ->
                val snackbarResut = scaffoldState.snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = "Retry",
                )
                when (snackbarResut) {
                    SnackbarResult.Dismissed -> {}
                    SnackbarResult.ActionPerformed -> {
                        viewModel.refreshImageUrls(breedId = breedId)
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(80.dp),
            ) {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterStart),
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
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = currentBreedId,
                    fontSize = 24.sp
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
        ) {
            viewState.let { state ->
                when (state) {
                    Initial -> {}
                    is Content -> DetailContent(
                        newItems = state.result,
                        onItemFavoriteClicked = { url, isFavorite ->
                            viewModel.updateImageFavoriteById(url, isFavorite)
                        }
                    )
                }
            }
        }
    }
}
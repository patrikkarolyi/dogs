package com.example.dogs.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dogs.ui.common.DetailContent
import com.example.dogs.ui.common.EmptyContent
import com.example.dogs.ui.details.DetailViewState.Content
import com.example.dogs.ui.details.DetailViewState.Initial
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailScreen(
    viewModel: DetailsViewModel = viewModel(),
    navController: NavController,
    breedId: String
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val viewState by viewModel.uiState.collectAsState()
    val refreshing = viewModel.isRefreshing
    val pullRefreshState = rememberPullRefreshState(refreshing, {viewModel.refreshImageUrls() })
    val title = viewModel.title

    if (breedId.isNotBlank()) {
        LaunchedEffect(coroutineScope) {
            viewModel.refreshImageUrls(breedId = breedId)
            viewModel.errorMessage.collectLatest { message ->
                val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = "retry"
                )
                when (snackbarResult) {
                    SnackbarResult.Dismissed -> {}
                    SnackbarResult.ActionPerformed -> {
                        viewModel.refreshImageUrls()
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
                    text = title,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .pullRefresh(pullRefreshState)
        ) {
            viewState.let { state ->
                when (state) {
                    Initial -> EmptyContent()
                    is Content -> DetailContent(
                        newItems = state.result,
                        onItemFavoriteClicked = { url, isFavorite ->
                            viewModel.updateImageFavoriteById(url, isFavorite)
                        }
                    )
                }
            }
            //TODO make pull to refresh work on EmptyContent
            PullRefreshIndicator(
                refreshing,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter)
            )
        }
    }
}
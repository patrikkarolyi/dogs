package com.example.dogs.ui.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dogs.data.presentation.ImagePresentationModel
import com.example.dogs.data.presentation.NetworkErrorPresentationModel
import com.example.dogs.ui.common.ImageListContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel = hiltViewModel(),
    navController: NavController,
    breedId: String,
    title: String
) {
    val viewState by viewModel.uiState.collectAsState()
    val error by viewModel.errorMessage.collectAsState(initial = null)

    if (breedId.isNotBlank()) {
        LaunchedEffect(Unit) {
            viewModel.refreshImageUrls(breedId)
        }
    }

    DetailsScreen(
        navController = navController,
        error = error,
        title = title,
        newItems = viewState.result,
        isRefreshing = viewState.isRefreshing,
        onItemFavoriteClicked = viewModel::updateImageFavoriteByUrl,
        onRefreshTriggered = viewModel::refreshImageUrls
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class)
@Composable
fun DetailsScreen(
    navController: NavController,
    error: NetworkErrorPresentationModel?,
    title: String,
    newItems: List<ImagePresentationModel> = emptyList(),
    isRefreshing: Boolean,
    onRefreshTriggered: () -> Unit,
    onItemFavoriteClicked: (String, Boolean) -> Unit,
) {
    val pullRefreshState = rememberPullRefreshState(isRefreshing, onRefreshTriggered)
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val sbHostState = remember { SnackbarHostState() }
    val context = LocalContext.current


    LaunchedEffect(error) {
        error?.let {
            coroutineScope.launch {
                sbHostState.showSnackbar(
                    error.message.asString(context)
                )
            }
        }
    }

    Scaffold(
        modifier = Modifier.padding(top = 20.dp),
        snackbarHost = { SnackbarHost(sbHostState) },
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(80.dp),
            ) {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterStart),
                    onClick = {
                        navController.navigateUp()
                    }
                ) {
                    Icon(
                        tint = MaterialTheme.colorScheme.secondary,
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
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
            ImageListContent(
                newItems = newItems,
                onItemFavoriteClicked = onItemFavoriteClicked
            )
            PullRefreshIndicator(
                isRefreshing,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter)
            )
        }
    }
}
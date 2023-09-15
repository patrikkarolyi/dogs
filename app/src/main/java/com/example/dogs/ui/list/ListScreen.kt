package com.example.dogs.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dogs.R
import com.example.dogs.navigation.Screen
import com.example.dogs.ui.custom_view.ListContent
import com.example.dogs.ui.custom_view.MySearchToolbar
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListScreen(
    viewModel: ListViewModel = viewModel(),
    navController: NavController,
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val refreshing = viewModel.isRefreshing
    val pullRefreshState = rememberPullRefreshState(refreshing, { viewModel.refreshAllBreeds() })
    var text by rememberSaveable { mutableStateOf("") }
    var showSettingsDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colors.background),
        scaffoldState = scaffoldState,
        topBar = {
            Row(
                verticalAlignment = CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (showSettingsDialog) {
                    SettingsDialog(
                        navController = navController,
                        onButtonClick = {
                            showSettingsDialog = false
                        },
                    )
                }
                MySearchToolbar(
                    modifier = Modifier.weight(1f),
                    onSearchQueryChanged = { text = it },
                    searchQuery = text,
                    onSearchTriggered = { viewModel.updateFilters(it) }
                )
                IconButton(
                    onClick = { showSettingsDialog = true }
                ) {
                    Icon(
                        tint = MaterialTheme.colors.secondary,
                        imageVector = Icons.Rounded.Favorite,
                        contentDescription = "Favorites"
                    )
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .pullRefresh(pullRefreshState)
        ) {
            when (val state = viewModel.uiState) {
                is ListViewState.Content -> ListContent(
                    state.result,
                    { id ->
                        navController.navigate(Screen.DetailScreen.withArgs(id))
                    },
                    viewModel::updateBreedFavoriteById
                )

                ListViewState.Initial -> {}
            }
            PullRefreshIndicator(
                refreshing,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter)
            )
        }
        LaunchedEffect(coroutineScope) {
            viewModel.getAllBreeds()
            viewModel.errorMessage.collect { message ->
                val snackbarResut = scaffoldState.snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = "Retry",
                )
                when (snackbarResut) {
                    SnackbarResult.Dismissed -> {}
                    SnackbarResult.ActionPerformed -> {
                        viewModel.refreshAllBreeds()
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsDialog(
    navController: NavController,
    onButtonClick: () -> Unit
) {

    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface),
        onDismissRequest = { onButtonClick() },
        title = {
            Text(
                text = stringResource(R.string.favorites),
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.onSurface
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)

            ) {
                IconButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.primary),
                    onClick = {
                        navController.navigate(Screen.FavDogScreen.route)
                        onButtonClick()
                    }
                ) {
                    Text(
                        color = MaterialTheme.colors.onPrimary,
                        text = stringResource(R.string.dogs)
                    )
                }
                IconButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.primary),
                    onClick = {
                        navController.navigate(Screen.FavImgScreen.route)
                        onButtonClick()
                    }
                ) {
                    Text(
                        color = MaterialTheme.colors.onPrimary,
                        text = stringResource(R.string.images)
                    )
                }
            }
        },
        confirmButton = {
            Text(
                text = stringResource(R.string.cancel),
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onButtonClick() },
            )
        },
    )
}
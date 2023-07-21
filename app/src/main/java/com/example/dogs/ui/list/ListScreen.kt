package com.example.dogs.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dogs.R
import com.example.dogs.data.disk.model.RoomBreedData
import com.example.dogs.navigation.NavDirection
import com.example.dogs.ui.customViews.MySearchToolbar
import kotlinx.coroutines.CoroutineScope


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListScreen(viewModel: ListViewModel = viewModel(), onItemClicked: (String) -> Unit) {
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
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (showSettingsDialog) {
                    SettingsDialog(
                        viewModel = viewModel,
                        onDismiss = {
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
                is ListViewState.Content -> ListContent(state.result, onItemClicked)
                ListViewState.Initial -> {}
            }
            PullRefreshIndicator(
                refreshing,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter)
            )
        }
        LaunchedEffect(coroutineScope) {
            viewModel.errorMessage.collect() { message ->
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
fun SettingsDialog(viewModel: ListViewModel, onDismiss: () -> Unit) {
    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = stringResource(R.string.favorites),
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.onBackground
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                IconButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.primary),
                    onClick = {
                        viewModel.navigateTo(navTo = NavDirection.ToFavoriteDogs)
                    }
                ) {
                    Text(
                        color = MaterialTheme.colors.onPrimary,
                        text = stringResource(R.string.dogs)
                    )
                }
                Divider()
                IconButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.primary),
                    onClick = { viewModel.navigateTo(navTo = NavDirection.ToFavoriteImages) }
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
                    .clickable { onDismiss() },
            )
        },
    )
}


@Composable
fun ListContent(
    newItems: List<RoomBreedData> = emptyList(),
    onItemClicked: (String) -> Unit
) {
    LazyColumn {
        items(newItems) { item ->
            ListItemContent(
                item = item,
                onItemClicked = onItemClicked
            )
        }
    }
}

@Composable
fun ListItemContent(
    item: RoomBreedData,
    onItemClicked: (String) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .background(MaterialTheme.colors.background)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.surface)
                .padding(5.dp)
                .clickable {
                    onItemClicked(item.breedName)
                }
        ) {
            Column(
                Modifier
                    .background(MaterialTheme.colors.primary)
                    .padding(5.dp)
            ) {
                Text(
                    text = item.breedName,
                    color = MaterialTheme.colors.onPrimary
                )
            }
        }
    }
}
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
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dogs.data.disk.model.RoomBreedData
import com.example.dogs.navigation.TopLevelDestination
import com.example.dogs.ui.customViews.MyBottomBar
import com.example.dogs.ui.customViews.MySearchToolbar
import com.example.dogs.ui.theme.DogsTheme
import kotlinx.coroutines.CoroutineScope


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListScreen(viewModel: ListViewModel = viewModel(), onItemClicked: (String) -> Unit) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val refreshing = viewModel.isRefreshing
    val pullRefreshState = rememberPullRefreshState(refreshing, { viewModel.refreshAllBreeds() })
    val navController: NavController = rememberNavController()
    var text by rememberSaveable { mutableStateOf("") }


    DogsTheme {
        Scaffold(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
            scaffoldState = scaffoldState,
            topBar = {
                MySearchToolbar(
                    modifier = Modifier,
                    onSearchQueryChanged = { text = it },
                    searchQuery = text,
                    onSearchTriggered = { viewModel.updateFilters(it) }
                )
            },
            bottomBar = {
                MyBottomBar(
                    destinations = TopLevelDestination.values().asList(),
                    onNavigateToDestination = {},
                    currentDestination = navController
                        .currentBackStackEntryAsState().value?.destination,
                )
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
        }
    }

}

fun showErrorSnackbar() {
    /*LaunchedEffect(coroutineScope) {
        coroutineScope.launch {
            val snackbarResut = scaffoldState.snackbarHostState.showSnackbar(
                message = "Action clicked!.",
                actionLabel = "Retry",
            )
            when (snackbarResut) {
                SnackbarResult.Dismissed -> {}
                SnackbarResult.ActionPerformed -> {}
            }
        }
    }*/
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
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(5.dp)
                .clickable {
                    onItemClicked(item.breedName)
                }
        ) {
            Column(
                Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(5.dp)
            ) {
                Text(
                    text = item.breedName,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
package com.example.dogs.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dogs.navigation.Screen
import com.example.dogs.ui.custom_view.ListContent
import com.example.dogs.ui.custom_view.MyNavDrawerView
import com.example.dogs.ui.custom_view.MySearchToolbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    MyNavDrawerView(
        drawerState = drawerState,
        navController = navController
    ) {
        Scaffold(
            modifier = Modifier
                .background(MaterialTheme.colors.background),
            scaffoldState = scaffoldState,
            topBar = {
                Row(
                    verticalAlignment = CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }
                    ) {
                        Icon(
                            tint = MaterialTheme.colors.secondary,
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                    MySearchToolbar(
                        onSearchTriggered = { viewModel.updateFilters(it) }
                    )
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
}
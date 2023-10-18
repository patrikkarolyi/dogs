package com.example.dogs.ui.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
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
import com.example.dogs.data.presentation.DogPresentationModel
import com.example.dogs.data.presentation.NetworkErrorPresentationModel
import com.example.dogs.navigation.Screen
import com.example.dogs.ui.common.ListContent
import com.example.dogs.ui.common.NavDrawer
import com.example.dogs.ui.common.SearchBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ListScreen(
    viewModel: ListViewModel = hiltViewModel(),
    navController: NavController,
) {
    val viewState by viewModel.uiState.collectAsState()
    val error by viewModel.errorMessage.collectAsState(initial = null)

    ListScreen(
        navController = navController,
        error = error,
        newItems = viewState.result,
        isRefreshing = viewState.isRefreshing,
        onRefreshTriggered = viewModel::refreshAllBreeds,
        onSearchTriggered = viewModel::updateFilters,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListScreen(
    navController: NavController,
    error: NetworkErrorPresentationModel?,
    newItems: List<DogPresentationModel> = emptyList(),
    isRefreshing: Boolean = false,
    onRefreshTriggered: () -> Unit,
    onSearchTriggered: (String) -> Unit,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val pullRefreshState = rememberPullRefreshState(isRefreshing, onRefreshTriggered)
    val context = LocalContext.current
    val sbHostState = remember { SnackbarHostState() }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    LaunchedEffect(error) {
        error?.let {
            coroutineScope.launch {
                sbHostState.showSnackbar(
                    message = error.getStringMessage(context)
                )
            }
        }
    }

    NavDrawer(
        drawerState = drawerState,
        navController = navController,
    ) {
        Scaffold(
            modifier = Modifier.padding(top = 20.dp),
            snackbarHost = { SnackbarHost(sbHostState) },
            topBar = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
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
                            tint = MaterialTheme.colorScheme.secondary,
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                    SearchBar(
                        onSearchTriggered = onSearchTriggered,
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
                ListContent(newItems) { breedId, fullName ->
                    navController.navigate(Screen.DetailsScreen.withArgs(breedId, fullName))
                }
                PullRefreshIndicator(
                    refreshing = isRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}
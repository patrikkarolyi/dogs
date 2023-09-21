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
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dogs.navigation.Screen
import com.example.dogs.ui.common.ListContent
import com.example.dogs.ui.common.NavDrawer
import com.example.dogs.ui.common.SearchBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListScreen(
    viewModel: ListViewModel = hiltViewModel(),
    navController: NavController,
) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val sbHostState = remember { SnackbarHostState() }
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val pullRefreshState = rememberPullRefreshState(isRefreshing, viewModel::refreshAllBreeds)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val viewState by viewModel.uiState.collectAsState()
    val filter by viewModel.currentFilter.collectAsState()

    LaunchedEffect(coroutineScope) {
        viewModel.errorMessage.collectLatest { message ->
            coroutineScope.launch {
                sbHostState.showSnackbar(
                    message
                )
            }
        }
    }

    NavDrawer(
        drawerState = drawerState,
        navController = navController,
    ) {
        Scaffold(
            modifier = Modifier
                .padding(top = 20.dp),
            snackbarHost = { SnackbarHost(sbHostState) },
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
                            tint = MaterialTheme.colorScheme.secondary,
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                    SearchBar(
                        onSearchTriggered = { viewModel.updateFilters(it) },
                        text = filter,
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
                ListContent(viewState.result) { breedId, fullName ->
                    navController.navigate(Screen.DetailScreen.withArgs(breedId,fullName))
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
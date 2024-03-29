package com.example.dogs.ui.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dogs.data.presentation.ImagePresentationModel
import com.example.dogs.ui.common.ImageListContent
import com.example.dogs.ui.common.NavDrawer
import com.example.dogs.ui.common.SearchBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun FavoriteScreen(
    viewModel: FavoriteViewModel = hiltViewModel(),
    navController: NavController,
) {
    val viewState by viewModel.uiState.collectAsState()

    FavoriteScreen(
        navController = navController,
        newItems = viewState.result,
        onSearchTriggered = viewModel::updateFilters,
        onItemFavoriteClicked = viewModel::updateImageFavoriteByUrl
    )
}

@Composable
fun FavoriteScreen(
    newItems: List<ImagePresentationModel> = emptyList(),
    navController: NavController,
    onSearchTriggered: (String) -> Unit,
    onItemFavoriteClicked: (String, Boolean) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    NavDrawer(
        drawerState = drawerState,
        navController = navController,
    ) {
        Scaffold(
            modifier = Modifier.padding(top = 20.dp),
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
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
            },
        ) {
            Box(
                modifier = Modifier
                    .padding(it)
            ) {
                ImageListContent(
                    newItems = newItems,
                    onItemFavoriteClicked = onItemFavoriteClicked
                )
            }
        }
    }
}
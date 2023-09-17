package com.example.dogs.ui.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dogs.ui.common.DetailContent
import com.example.dogs.ui.common.MyNavDrawerView
import com.example.dogs.ui.common.MySearchToolbar
import com.example.dogs.ui.favorite.FavImgViewState.Content
import com.example.dogs.ui.favorite.FavImgViewState.Initial
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun FavImgScreen(
    viewModel: FavImgViewModel = viewModel(),
    navController: NavController,
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    LaunchedEffect(Unit) {
        viewModel.getFavoriteImageUrls()
    }


    MyNavDrawerView(
        modifier = Modifier,
        drawerState = drawerState,
        navController = navController,
    ) {
        Scaffold(
            modifier = Modifier
                .background(MaterialTheme.colors.background),
            scaffoldState = scaffoldState,
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
                            tint = MaterialTheme.colors.secondary,
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                    MySearchToolbar(
                        onSearchTriggered = { viewModel.updateFilters(it) }
                    )
                }
            },
            bottomBar = {
/*                BottomBar(
                    navController = navController,
                    modifier = Modifier,
                )*/
            }
        ) {
            Box(
                modifier = Modifier
                    .padding(it)
            ) {
                when (val state = viewModel.uiState) {
                    is Content -> DetailContent(
                        newItems = state.result,
                        onItemFavoriteClicked = { url, isFavorite ->
                            viewModel.updateImageFavoriteById(url, isFavorite)
                        }
                    )

                    Initial -> {}
                }
            }
        }
    }
}
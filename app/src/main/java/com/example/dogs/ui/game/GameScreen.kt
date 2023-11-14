package com.example.dogs.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dogs.R
import com.example.dogs.data.presentation.NetworkErrorPresentationModel
import com.example.dogs.ui.common.NavDrawer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun GameScreen(
    viewModel: GameViewModel = hiltViewModel(),
    navController: NavController,
) {
    val state by viewModel.uiState.collectAsState()
    val error by viewModel.errorMessage.collectAsState(initial = null)

    GameScreen(
        state = state,
        error = error,
        navController = navController,
        onAnswerClicked = viewModel::submitAnswer,
        onResetClicked = viewModel::resetQuestion
    )
}

@Composable
fun GameScreen(
    state: GameViewState,
    error: NetworkErrorPresentationModel?,
    navController: NavController,
    onAnswerClicked: (String) -> Boolean,
    onResetClicked: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val sbHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(error) {
        error?.let {
            coroutineScope.launch {
                sbHostState.showSnackbar(
                    message = error.message.asString(context)
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
                }
            },
        ) {
            when(state){
                GameViewState.Loading ->
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(text = "Loading ...", modifier = Modifier.align(Alignment.Center))
                }

                is GameViewState.GameViewContent ->
                    Column(
                        modifier = Modifier
                            .padding(it),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = "Guess which dog is on the picture!")
                        GameAnswerList(
                            content = state,
                            onAnswerClicked = onAnswerClicked
                        )
                        Box(
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            Button(
                                onClick = onResetClicked,
                                modifier = Modifier.align(Alignment.BottomCenter)) {
                                Text(text = stringResource(R.string.reset))
                            }
                        }
                    }

            }
        }
    }
}





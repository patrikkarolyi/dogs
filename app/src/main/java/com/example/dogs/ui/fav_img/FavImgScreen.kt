package com.example.dogs.ui.fav_img

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dogs.ui.custom_view.DetailContent
import com.example.dogs.ui.fav_img.FavImgViewState.Content
import com.example.dogs.ui.fav_img.FavImgViewState.Initial

@Composable
fun FavImgScreen(
    viewModel: FavImgViewModel = viewModel(),
    onItemFavoriteClicked: (String, Boolean) -> Unit,
    onNavBack: () -> Unit
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colors.background),
        scaffoldState = scaffoldState,
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                IconButton(
                    onClick = {
                        onNavBack()
                    }
                ) {
                    Icon(
                        tint = MaterialTheme.colors.secondary,
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
        ) {
            when (val state = viewModel.uiState) {
                is Content -> DetailContent(state.result, onItemFavoriteClicked)
                Initial -> {}
            }
        }
    }
}
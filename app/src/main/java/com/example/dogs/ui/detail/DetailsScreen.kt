package com.example.dogs.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.dogs.R
import com.example.dogs.data.disk.model.RoomImageData
import com.example.dogs.ui.detail.DetailsViewState.*
import kotlinx.coroutines.CoroutineScope


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel = viewModel(),
    onItemFavoriteClicked: (String, Boolean) -> Unit,
    onNavBack: () -> Unit
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val breedId = (viewModel.uiState as? Content)?.breedId ?: ""

    if (breedId.isNotBlank()) {
        LaunchedEffect(coroutineScope) {
            viewModel.errorMessage.collect { message ->
                val snackbarResut = scaffoldState.snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = "Retry",
                )
                when (snackbarResut) {
                    SnackbarResult.Dismissed -> {}
                    SnackbarResult.ActionPerformed -> {
                        viewModel.refreshImageUrls(breedId)
                    }
                }
            }
        }
    }


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
                is Content -> DetailsContent(state.result, onItemFavoriteClicked)
                Initial -> {}
            }
        }
    }
}

@Composable
fun DetailsContent(
    newItems: List<RoomImageData> = emptyList(),
    onItemFavoriteClicked: (String, Boolean) -> Unit
) {
    LazyColumn {
        items(newItems) { item ->
            DetailsItemContent(
                item = item,
                onItemFavoriteClicked = onItemFavoriteClicked
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailsItemContent(
    item: RoomImageData,
    onItemFavoriteClicked: (String, Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        GlideImage(
            model = item.url,
            contentDescription = "image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        ) {
            it
                .placeholder(R.drawable.place_holder)
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .background(MaterialTheme.colors.primary)
                .padding(5.dp)

        ) {
            Text(
                text = item.breedId,
                color = MaterialTheme.colors.onPrimary
            )
        }
        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd),
            onClick = { onItemFavoriteClicked(item.url, !item.isFavorite) }
        ) {
            Icon(
                tint = MaterialTheme.colors.secondary,
                imageVector = if (item.isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                contentDescription = "Favorites"
            )
        }

    }
}
package com.example.dogs.ui.fav_dog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dogs.data.disk.model.RoomBreedData
import com.example.dogs.ui.custom_view.MySearchToolbar
import com.example.dogs.ui.fav_dog.FavDogViewState.*


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FavoriteDogsScreen(
    viewModel: FavDogViewModel = viewModel(),
    onItemClicked: (String) -> Unit,
    onItemFavoriteClicked: (String, Boolean) -> Unit
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
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
        ) {
            when (val state = viewModel.uiState) {
                is Content -> FavoriteDogsContent(
                    state.result,
                    onItemClicked,
                    onItemFavoriteClicked
                )

                Initial -> {}
            }
        }
    }
}

@Composable
fun FavoriteDogsContent(
    newItems: List<RoomBreedData> = emptyList(),
    onItemClicked: (String) -> Unit,
    onItemFavoriteClicked: (String, Boolean) -> Unit
) {
    LazyColumn {
        items(newItems) { item ->
            FavoriteDogsItemContent(
                item = item,
                onItemClicked = onItemClicked,
                onItemFavoriteClicked = onItemFavoriteClicked
            )
        }
    }
}

@Composable
fun FavoriteDogsItemContent(
    item: RoomBreedData,
    onItemClicked: (String) -> Unit,
    onItemFavoriteClicked: (String, Boolean) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
            .padding(horizontal = 10.dp)
            .clickable {
                onItemClicked(item.id)
            }
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f),
            text = "${item.subBreedName} ${item.breedName}".trim(),
            color = MaterialTheme.colors.onPrimary,

            )

        IconButton(
            onClick = { onItemFavoriteClicked(item.id, !item.isFavorite) }
        ) {
            Icon(
                tint = MaterialTheme.colors.secondary,
                imageVector = if (item.isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                contentDescription = "Favorites"
            )
        }
    }
}
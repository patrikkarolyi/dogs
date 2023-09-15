package com.example.dogs.ui.custom_view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dogs.data.disk.model.RoomBreedData


@Composable
fun ListContent(
    newItems: List<RoomBreedData> = emptyList(),
    onItemClicked: (String) -> Unit,
    onItemFavoriteClicked: (String, Boolean) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(newItems) { item ->
            ListItemContent(
                item = item,
                onItemClicked = onItemClicked,
                onItemFavoriteClicked = onItemFavoriteClicked
            )
        }
    }
}

@Composable
fun ListItemContent(
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
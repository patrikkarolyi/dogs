package com.example.dogs.ui.custom_view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.dogs.R
import com.example.dogs.data.disk.model.RoomImageData


@Composable
fun DetailContent(
    newItems: List<RoomImageData> = emptyList(),
    onItemFavoriteClicked: (String, Boolean) -> Unit
) {
    LazyColumn {
        items(newItems) { item ->
            DetailItemContent(
                item = item,
                onItemFavoriteClicked = onItemFavoriteClicked
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailItemContent(
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
package com.example.dogs.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dogs.data.disk.model.RoomBreedData


@Composable
fun ListContent(
    newItems: List<RoomBreedData> = emptyList(),
    onItemClicked: (String) -> Unit,
) {
    LazyColumn(
    ) {
        items(newItems) { item ->
            ListItemContent(
                item = item,
                onItemClicked = onItemClicked,
            )
        }
    }
}

@Composable
fun ListItemContent(
    item: RoomBreedData,
    onItemClicked: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onItemClicked(item.id)
            },
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp),
            text = "${item.subBreedName} ${item.breedName}".trim(),
        )
    }
}
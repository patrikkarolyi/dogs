package com.example.dogs.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.dogs.data.presentation.DogPresentationModel
import com.example.dogs.util.TestTags


@Composable
fun ListContent(
    newItems: List<DogPresentationModel> = emptyList(),
    onItemClicked: (String, String) -> Unit,
) {
    if (newItems.isEmpty()) {
        EmptyContent()
    } else {
        LazyColumn(
            modifier = Modifier.testTag(TestTags.LIST_CONTENT)
        ) {
            items(newItems) { item ->
                ListItemContent(
                    item = item,
                    onItemClicked = onItemClicked,
                )
            }
        }
    }
}

@Composable
private fun ListItemContent(
    item: DogPresentationModel,
    onItemClicked: (String, String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onItemClicked(item.breedId, item.fullName)
            },
    ) {
        Text(
            text = item.fullName,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(16.dp),
        )
    }
}
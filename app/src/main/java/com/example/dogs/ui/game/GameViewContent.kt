package com.example.dogs.ui.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.dogs.R
import com.example.dogs.data.presentation.DogPresentationModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GameAnswerList(
    content: GameViewState.GameViewContent,
    onAnswerClicked: (String) -> Boolean
) {
    Column {
        GlideImage(
            model = content.image?.url ?: "",
            contentDescription = "image",
            contentScale = ContentScale.FillHeight,
            requestBuilderTransform = {
                it
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_error)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
        LazyColumn {
            items(content.dogNames) { item ->
                GameAnswerListItem(
                    dog = item,
                    onAnswerClicked = onAnswerClicked
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameAnswerListItem(
    dog: DogPresentationModel,
    onAnswerClicked: (String) -> Boolean
) {
    val defaultColor = MaterialTheme.colorScheme.surface
    val greenColor = MaterialTheme.colorScheme.primary
    val redColor = MaterialTheme.colorScheme.error
    val color = remember { mutableStateOf(defaultColor) }

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.value,
        ),
        onClick = {
            color.value = if (onAnswerClicked(dog.breedId)) greenColor else redColor
        }
    ) {
        Text(
            text = dog.fullName,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(16.dp),
        )
    }
}
package com.example.dogs.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import com.example.dogs.R

@Composable
fun EmptyContent() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_empty),
            contentDescription = "",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .align(Alignment.Center)
        )
    }
}
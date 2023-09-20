package com.example.dogs.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.dogs.R
import kotlinx.coroutines.delay

@Composable
fun EmptyContent() {
    var isVisible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        delay(1000)
        isVisible = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = isVisible,
            exit = fadeOut(animationSpec = tween(2000, easing = LinearEasing)),
            enter = fadeIn(animationSpec = tween(2000, easing = LinearEasing)),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = stringResource(R.string.no_content),
                color = MaterialTheme.colorScheme.onBackground,
                )
        }
    }
}
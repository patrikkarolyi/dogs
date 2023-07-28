package com.example.dogs.ui.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Purple700,
    primaryVariant = Purple500,
    secondary = Teal200,
    secondaryVariant = Teal700,
    background = Black,
    surface = Black,
    error = Red,
    onPrimary = White,
    onSecondary = White,
    onBackground = White,
    onSurface = White,
    onError = White,
)

private val LightColorPalette = lightColors(
    primary = Blue400,
    primaryVariant = Blue600,
    secondary = Orange400,
    secondaryVariant = Orange200,
    background = LightGray,
    surface = LightGray,
    error = Red,
    onPrimary = Black,
    onSecondary = Black,
    onBackground = Black,
    onSurface = Black,
    onError = Black,
)

@Composable
fun DogsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

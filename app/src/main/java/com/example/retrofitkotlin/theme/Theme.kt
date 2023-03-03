package com.example.retrofitkotlin.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = lightColors(
    primary = colorPrimary,
    primaryVariant = colorPrimary,
    onPrimary = colorPrimary,
    secondary = Color.White,
    onSecondary = colorPrimary,
    background = Color(0xFFEEEEEE),
    onBackground = colorPrimary,
    surface = Color.White,
    onSurface = colorPrimary,
    error = Color(0xFFD00036),
    onError = Color.Red
)

@Composable
fun MovieTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        DarkColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes
    ) {
        content()
    }
}
package com.atitienei_daniel.reeme.presentation.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ReemeTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        content = content,
        colors = lightColors,
        shapes = Shapes,
        typography = ReemeTypography
    )
}

private val lightColors = lightColors(
    primary = DarkBlue800,
    primaryVariant = DarkBlue700,
    onPrimary = Color.White,
    secondary = DarkBlue800,
    secondaryVariant = DarkBlue700,
    onSecondary = Color.White,
    background = Color.White,
    onSurface = DarkBlue800
)
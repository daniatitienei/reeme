package com.atitienei_daniel.reeme.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ReemeTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        content = content,
        colors = if (isDarkTheme) darkColors else lightColors,
        shapes = Shapes,
        typography = ReemeTypography(isDarkTheme = isDarkTheme)
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
    onSurface = DarkBlue800,
    error = Red800
)

private val darkColors = darkColors(
    primary = DarkBlue500,
    primaryVariant = DarkBlue400,
    onPrimary = Color.White,
    secondary = DarkBlue500,
    secondaryVariant = DarkBlue400,
    onSecondary = Color.White,
    onSurface = DarkBlue500,
    error = Red800
)
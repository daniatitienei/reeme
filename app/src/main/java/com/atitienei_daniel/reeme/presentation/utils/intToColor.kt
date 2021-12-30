package com.atitienei_daniel.reeme.presentation.utils

import androidx.compose.ui.graphics.Color
import com.atitienei_daniel.reeme.presentation.theme.*

fun intToColor(value: Int): Color =
    when (value) {
        Red900.hashCode() -> Red900
        Blue900.hashCode() -> Blue900
        Yellow900.hashCode() -> Yellow900
        Purple900.hashCode() -> Purple900
        Orange900.hashCode() -> Orange900
        else -> Lime900
    }

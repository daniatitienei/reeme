package com.atitienei_daniel.reeme.presentation.utils

import androidx.compose.ui.graphics.Color
import com.atitienei_daniel.reeme.presentation.theme.*

fun stringToColor(text: String): Color =
    when (text) {
        Red900.hashCode().toString() -> Red900
        Blue900.hashCode().toString() -> Blue900
        Yellow900.hashCode().toString() -> Yellow900
        Magenta900.hashCode().toString() -> Magenta900
        Orange900.hashCode().toString() -> Orange900
        else -> Lime900
    }

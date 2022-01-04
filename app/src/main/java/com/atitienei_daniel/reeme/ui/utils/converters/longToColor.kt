package com.atitienei_daniel.reeme.ui.utils

import androidx.compose.ui.graphics.Color
import com.atitienei_daniel.reeme.ui.theme.*

fun longToColor(value: Long): Color =
    when (value) {
        Red800.value.toLong() -> Red800
        Blue900.value.toLong() -> Blue900
        Yellow900.value.toLong() -> Yellow900
        Purple900.value.toLong() -> Purple900
        Orange900.value.toLong() -> Orange900
        else -> Lime900
    }

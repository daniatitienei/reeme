
package com.atitienei_daniel.reeme.presentation.utils

import java.util.*

fun Date.dateToString(format: String): String {
    val dateFormatter = java.text.SimpleDateFormat(format, Locale.getDefault())

    return dateFormatter.format(this)
}
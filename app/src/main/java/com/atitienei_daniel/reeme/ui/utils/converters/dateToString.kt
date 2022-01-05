
package com.atitienei_daniel.reeme.ui.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.dateToString(format: String): String {
    val dateFormatter = SimpleDateFormat(format, Locale.getDefault())

    return dateFormatter.format(this)
}
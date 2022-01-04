package com.atitienei_daniel.reeme.ui.utils.components

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.runtime.Composable
import java.util.*

@Composable
fun ShowTimePicker(
    context: Context,
    onTimePicked: (String, Int, Int) -> Unit,
    onDismissRequest: () -> Unit
) {
    val calendar = Calendar.getInstance()
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minuteValue ->
            onTimePicked(
                "${"$hourOfDay".padStart(2, '0')}:${"$minuteValue".padStart(2, '0')}",
                hourOfDay,
                minuteValue
            )
        }, hour, minute, false
    )

    timePickerDialog.setOnDismissListener {
        onDismissRequest()
    }

    timePickerDialog.show()
}
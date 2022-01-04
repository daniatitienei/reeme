package com.atitienei_daniel.reeme.ui.utils.components

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.runtime.Composable
import java.util.*

@Composable
fun ShowDatePicker(
    context: Context,
    onDatePicked: (String, Int, Int, Int) -> Unit,
    onDismissRequest: () -> Unit
) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    calendar.time = Date()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, yearValue, monthValue, dayOfMonth ->
            onDatePicked(
                "${"$dayOfMonth".padStart(2, '0')}/${
                    "${monthValue + 1}".padStart(
                        2,
                        '0'
                    )
                }/$yearValue",
                yearValue,
                monthValue,
                dayOfMonth
            )
        }, year, month, day
    )

    datePickerDialog.setOnDismissListener {
        onDismissRequest()
    }

    datePickerDialog.datePicker.minDate = Calendar.getInstance().timeInMillis

    datePickerDialog.show()
}
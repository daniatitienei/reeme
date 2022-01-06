package com.atitienei_daniel.reeme.ui.utils.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.runtime.Composable
import com.atitienei_daniel.reeme.ui.screens.create_reminder.OutlinedPicker
import com.atitienei_daniel.reeme.ui.utils.enums.ReminderRepeatTypes

@Composable
fun RepeatDropdown(
    repeat: ReminderRepeatTypes,
    onRepeatValueChange: (ReminderRepeatTypes) -> Unit,
    showRepeatDropdownMenu: Boolean,
    toggleDropDown: (Boolean) -> Unit
) {
    Column {
        OutlinedPicker(
            value = when (repeat) {
                ReminderRepeatTypes.ONCE -> "Once"
                ReminderRepeatTypes.DAILY -> "Daily"
                ReminderRepeatTypes.WEEKLY -> "Weekly"
                ReminderRepeatTypes.MONTHLY -> "Monthly"
                ReminderRepeatTypes.YEARLY -> "Yearly"
                else -> ""
            },
            placeholder = "Repeat",
            trailingIcon = Icons.Outlined.Repeat,
            onClick = { toggleDropDown(true) }
        )

        DropdownMenu(
            expanded = showRepeatDropdownMenu,
            onDismissRequest = { toggleDropDown(false) }
        ) {
            DropdownMenuItem(onClick = {
                onRepeatValueChange(ReminderRepeatTypes.UNSELECTED)
                toggleDropDown(false)
            }) {
                Text(text = "Remove")
            }

            DropdownMenuItem(onClick = {
                onRepeatValueChange(ReminderRepeatTypes.ONCE)
                toggleDropDown(false)
            }) {
                Text(text = "Once")
            }

            DropdownMenuItem(onClick = {
                onRepeatValueChange(ReminderRepeatTypes.DAILY)
                toggleDropDown(false)
            }) {
                Text(text = "Daily")
            }

            DropdownMenuItem(onClick = {
                onRepeatValueChange(ReminderRepeatTypes.WEEKLY)
                toggleDropDown(false)
            }) {
                Text(text = "Weekly")
            }

            DropdownMenuItem(onClick = {
                onRepeatValueChange(ReminderRepeatTypes.MONTHLY)
                toggleDropDown(false)
            }) {
                Text(text = "Monthly")
            }

            DropdownMenuItem(onClick = {
                onRepeatValueChange(ReminderRepeatTypes.YEARLY)
                toggleDropDown(false)
            }) {
                Text(text = "Yearly")
            }
        }
    }
}

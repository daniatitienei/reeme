package com.atitienei_daniel.reeme.ui.screens.reminders

import com.atitienei_daniel.reeme.domain.model.Reminder
import remindersdb.ReminderEntity

sealed class RemindersListEvents {
    object onSearchClick: RemindersListEvents()
    object onSettingsClick : RemindersListEvents()
    object onFilterClick : RemindersListEvents()
    data class onReminderClick(val reminder: ReminderEntity) : RemindersListEvents()
    object onAddClick : RemindersListEvents()
}
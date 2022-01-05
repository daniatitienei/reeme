package com.atitienei_daniel.reeme.ui.screens.reminders

import com.atitienei_daniel.reeme.domain.model.Reminder
import remindersdb.ReminderEntity

sealed class RemindersListEvents {
    object OnSearchClick: RemindersListEvents()
    object OnSettingsClick : RemindersListEvents()
    object OnFilterClick : RemindersListEvents()
    data class OnReminderClick(val reminder: ReminderEntity) : RemindersListEvents()
    object OnAddClick : RemindersListEvents()
}
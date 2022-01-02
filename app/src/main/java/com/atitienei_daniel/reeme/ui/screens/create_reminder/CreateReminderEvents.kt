package com.atitienei_daniel.reeme.ui.screens.create_reminder

import com.atitienei_daniel.reeme.domain.model.Reminder
import remindersdb.ReminderEntity

sealed class CreateReminderEvents {
    data class OnCreateReminderClick(val reminder: Reminder): CreateReminderEvents()
    object OnCancelClick: CreateReminderEvents()
    object OnCreateCategoryAlertClick: CreateReminderEvents()
    object OnCreateCategoryAlertDismiss: CreateReminderEvents()
}
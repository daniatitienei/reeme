package com.atitienei_daniel.reeme.ui.screens.create_reminder

import com.atitienei_daniel.reeme.domain.model.Reminder
import remindersdb.ReminderEntity

sealed class CreateReminderEvents {
    data class OnCreateReminderClick(val reminder: Reminder): CreateReminderEvents()
    object OnCancelClick: CreateReminderEvents()

    /* Alert dialog */
    object OpenCreateCategoryAlert: CreateReminderEvents()
    object DismissCreateCategoryAlert: CreateReminderEvents()

    /* Pickers */
    object OpenTimePicker: CreateReminderEvents()
    object DismissTimePicker: CreateReminderEvents()
    object OpenDatePicker: CreateReminderEvents()
    object DismissDatePicker: CreateReminderEvents()

    /* Dropdown */
    data class ToggleDropdown(val isOpen: Boolean): CreateReminderEvents()

    data class ToggleCheckBox(val isChecked: Boolean): CreateReminderEvents()

    data class InsertCategory(val categories: MutableList<String>): CreateReminderEvents()
}
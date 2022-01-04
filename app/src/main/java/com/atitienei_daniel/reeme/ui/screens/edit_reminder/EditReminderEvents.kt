package com.atitienei_daniel.reeme.ui.screens.edit_reminder

sealed class EditReminderEvents {
    /* Bottom app bar */
    object OnSaveClick: EditReminderEvents()
    object OnDeleteClick: EditReminderEvents()
    object OnDoneClick: EditReminderEvents()

    /* Alert dialog */
    object OpenCreateCategoryAlertDialog: EditReminderEvents()
    object DismissCreateCategoryAlert: EditReminderEvents()

    /* Pickers */
    object OpenDatePicker: EditReminderEvents()
    object DismissDatePicker: EditReminderEvents()
    object OpenTimePicker: EditReminderEvents()
    object DismissTimePicker: EditReminderEvents()

    /* Dropdown */
    data class ToggleDropdown(val isOpen: Boolean): EditReminderEvents()

    /* Checkbox */
    data class ToggleCheckBox(val isChecked: Boolean): EditReminderEvents()

    data class InsertCategory(val categories: MutableList<String>): EditReminderEvents()
}
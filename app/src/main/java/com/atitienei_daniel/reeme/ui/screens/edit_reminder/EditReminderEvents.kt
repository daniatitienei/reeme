package com.atitienei_daniel.reeme.ui.screens.edit_reminder

sealed class EditReminderEvents {
    object OnSaveClick: EditReminderEvents()
    object OnCloseClick: EditReminderEvents()
    object OnDoneClick: EditReminderEvents()
    object OnCreateCategoryAlertClick: EditReminderEvents()
    object OnCreateCategoryAlertDismiss: EditReminderEvents()
}
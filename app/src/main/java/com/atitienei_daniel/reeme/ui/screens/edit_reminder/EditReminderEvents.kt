package com.atitienei_daniel.reeme.ui.screens.edit_reminder

sealed class EditReminderEvents {
    object OnSaveClick: EditReminderEvents()
    object OnDeleteClick: EditReminderEvents()
    object OnDoneClick: EditReminderEvents()
    object OnCreateCategoryClick: EditReminderEvents()
    object OnCreateCategoryAlertDismiss: EditReminderEvents()
    object OnSelectDateClick: EditReminderEvents()
    object OnSelectDateDismiss: EditReminderEvents()
    object OnSelectTimeClick: EditReminderEvents()
    object OnSelectTimeDismiss: EditReminderEvents()
}
package com.atitienei_daniel.reeme.presentation.utils

sealed class Screens(val route: String) {
    object Reminders: Screens(route = "reminders_screen")
    object CreateReminder: Screens(route = "create_reminder_screen")
    object EditReminder: Screens(route = "edit_reminder_screen/{reminder}")
}

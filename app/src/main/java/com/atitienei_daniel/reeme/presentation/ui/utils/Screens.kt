package com.atitienei_daniel.reeme.presentation.ui.utils

sealed class Screens(val route: String) {
    object Register: Screens(route = "register_screen")
    object Login: Screens(route = "login_screen")
    object OnBoardingScreen: Screens(route = "on_boarding_screen")
    object Reminders: Screens(route = "reminders_screen")
    object CreateReminder: Screens(route = "create_reminders_screen")
}

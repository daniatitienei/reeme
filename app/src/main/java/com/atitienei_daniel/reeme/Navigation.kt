package com.atitienei_daniel.reeme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import com.atitienei_daniel.reeme.data.reminders_db.Reminder
import com.atitienei_daniel.reeme.presentation.ui.screens.create_reminder.CreateReminderScreen
import com.atitienei_daniel.reeme.presentation.ui.screens.edit_reminder.EditReminderScreen
import com.atitienei_daniel.reeme.presentation.ui.screens.reminders.RemindersScreen
import com.atitienei_daniel.reeme.presentation.utils.Screens
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.squareup.moshi.Moshi

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun Navigation(
    moshi: Moshi
) {
    val navController = rememberAnimatedNavController()

    Scaffold(
        backgroundColor = MaterialTheme.colors.background
    ) {
        AnimatedNavHost(
            navController = navController,
            startDestination = Screens.Reminders.route
        ) {
            composable(route = Screens.Reminders.route) {
                RemindersScreen(navController = navController, moshi = moshi)
            }

            composable(
                route = Screens.CreateReminder.route,
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { 1000 },
                        animationSpec = tween(700)
                    ) + fadeIn()
                },
                popEnterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { 1000 },
                        animationSpec = tween(700)
                    ) + fadeIn()
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { 1000 },
                        animationSpec = tween(700)
                    ) + fadeOut()
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { 1000 },
                        animationSpec = tween(700)
                    ) + fadeOut()
                }
            ) {
                CreateReminderScreen(navController = navController)
            }

            composable(
                route = Screens.EditReminder.route,
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { 1000 },
                        animationSpec = tween(700)
                    ) + fadeIn()
                },
                popEnterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { 1000 },
                        animationSpec = tween(700)
                    ) + fadeIn()
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { 1000 },
                        animationSpec = tween(700)
                    ) + fadeOut()
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { 1000 },
                        animationSpec = tween(700)
                    ) + fadeOut()
                }
            ) { backStackEntry ->
                val reminderJson = backStackEntry.arguments?.getString("reminder")
                val jsonAdapter = moshi.adapter(Reminder::class.java).lenient()
                val reminderObject = jsonAdapter.fromJson(reminderJson!!)

                EditReminderScreen(navController = navController, reminder = reminderObject!!)
            }
        }
    }
}
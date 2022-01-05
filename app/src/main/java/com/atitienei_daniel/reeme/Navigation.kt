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
import com.atitienei_daniel.reeme.domain.model.Reminder
import com.atitienei_daniel.reeme.ui.screens.create_reminder.CreateReminderScreen
import com.atitienei_daniel.reeme.ui.screens.edit_reminder.EditReminderScreen
import com.atitienei_daniel.reeme.ui.screens.reminders.RemindersListScreen
import com.atitienei_daniel.reeme.ui.screens.settings.SettingsScreen
import com.atitienei_daniel.reeme.ui.utils.Routes
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
    moshi: Moshi,
) {
    val navController = rememberAnimatedNavController()

    Scaffold(
        backgroundColor = MaterialTheme.colors.background
    ) {
        AnimatedNavHost(
            navController = navController,
            startDestination = Routes.REMINDERS
        ) {
            composable(route = Routes.REMINDERS) {
                RemindersListScreen(onNavigate = {
                    navController.navigate(it.route) {
                        launchSingleTop = true
                    }
                })
            }

            composable(
                route = Routes.CREATE_REMINDER,
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
                CreateReminderScreen(onPopBackStack = {
                    navController.popBackStack()
                })
            }

            composable(
                route = Routes.EDIT_REMINDER,
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
                EditReminderScreen(onPopBackStack = {
                    navController.popBackStack()
                })
            }

            composable(
                route = Routes.SETTINGS,
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
                SettingsScreen(onPopBackStack = {
                    navController.popBackStack()
                })
            }
        }
    }
}
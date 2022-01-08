package com.atitienei_daniel.reeme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.navDeepLink
import com.atitienei_daniel.reeme.domain.model.Reminder
import com.atitienei_daniel.reeme.domain.repository.StoreThemeRepository
import com.atitienei_daniel.reeme.ui.screens.create_reminder.CreateReminderScreen
import com.atitienei_daniel.reeme.ui.screens.edit_reminder.EditReminderScreen
import com.atitienei_daniel.reeme.ui.screens.reminders.RemindersListScreen
import com.atitienei_daniel.reeme.ui.screens.settings.SettingsScreen
import com.atitienei_daniel.reeme.ui.utils.Routes
import com.atitienei_daniel.reeme.ui.utils.enums.Theme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.squareup.moshi.Moshi

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun Navigation(
    repository: StoreThemeRepository
) {
    val currentTheme = repository.getTheme.collectAsState(initial = Theme.AUTO).value

    val systemTheme = isSystemInDarkTheme()
    val background = MaterialTheme.colors.background


    val navController = rememberAnimatedNavController()
    val systemUiController = rememberSystemUiController()
    val uri = "https://example.com"

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = background,
            darkIcons = when (currentTheme) {
                Theme.AUTO -> !systemTheme
                Theme.LIGHT -> true
                Theme.DARK -> false
            }
        )
    }

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
                deepLinks = listOf(
                    navDeepLink { uriPattern = "$uri/reminderId={reminderId}" }
                ),
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
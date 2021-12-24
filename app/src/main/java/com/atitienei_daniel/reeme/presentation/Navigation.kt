package com.atitienei_daniel.reeme.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.geometry.Offset
import com.atitienei_daniel.reeme.presentation.ui.screens.login.LoginScreen
import com.atitienei_daniel.reeme.presentation.ui.screens.on_boarding.OnBoardingScreen
import com.atitienei_daniel.reeme.presentation.ui.screens.register.RegisterScreen
import com.atitienei_daniel.reeme.presentation.ui.screens.reminders.RemindersScreen
import com.atitienei_daniel.reeme.presentation.ui.utils.Screens
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun Navigation() {
    val navController = rememberAnimatedNavController()

    Scaffold(
        backgroundColor = MaterialTheme.colors.background
    ) {

        AnimatedNavHost(
            navController = navController,
            startDestination = Screens.OnBoardingScreen.route
        ) {
            composable(
                route = Screens.OnBoardingScreen.route,
                enterTransition = {
                    slideInVertically(initialOffsetY = { 1000 }, animationSpec = tween(700)) +
                            fadeIn()
                },
                popEnterTransition = {
                    slideInVertically(initialOffsetY = { 1000 }, animationSpec = tween(700)) +
                            fadeIn()
                },
                exitTransition = {
                    slideOutVertically(targetOffsetY = { 1000 }, animationSpec = tween(700)) +
                            fadeOut()
                },
                popExitTransition = {
                    slideOutVertically(targetOffsetY = { 1000 }, animationSpec = tween(700)) +
                            fadeOut()
                }
            ) {
                OnBoardingScreen(navController = navController)
            }

            composable(
                route = Screens.Register.route,
                enterTransition = {
                    when (this.initialState.destination.route) {
                        Screens.Login.route ->
                            slideInHorizontally(
                                initialOffsetX = { -1000 },
                                animationSpec = tween(700)
                            ) + fadeIn()
                        else -> fadeIn(animationSpec = tween(700))
                    }
                },
                popEnterTransition = {
                    fadeIn(animationSpec = tween(700))
                },
                exitTransition = {
                    when (this.targetState.destination.route) {
                        Screens.Login.route ->
                            slideOutHorizontally(
                                targetOffsetX = { -1000 },
                                animationSpec = tween(700)
                            ) + fadeOut()
                        else -> fadeOut(animationSpec = tween(700))
                    }
                },
                popExitTransition = {
                    fadeOut(animationSpec = tween(700))
                }
            ) {
                RegisterScreen(navController = navController)
            }

            composable(
                route = Screens.Login.route,
                enterTransition = {
                    when (this.initialState.destination.route) {
                        Screens.Register.route ->
                            slideInHorizontally(
                                initialOffsetX = { -1000 },
                                animationSpec = tween(700)
                            ) + fadeIn()
                        else -> fadeIn(animationSpec = tween(700))
                    }
                },
                popEnterTransition = {
                    fadeIn(animationSpec = tween(700))
                },
                exitTransition = {
                    when (this.targetState.destination.route) {
                        Screens.Register.route ->
                            slideOutHorizontally(
                                targetOffsetX = { -1000 },
                                animationSpec = tween(700)
                            ) + fadeOut()
                        else -> fadeOut(animationSpec = tween(700))
                    }
                },
                popExitTransition = {
                    fadeOut(animationSpec = tween(700))
                }
            ) {
                LoginScreen(navController = navController)
            }

            composable(route = Screens.Reminders.route) {
                RemindersScreen()
            }
        }
    }
}
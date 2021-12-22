package com.atitienei_daniel.reeme.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.geometry.Offset
import com.atitienei_daniel.reeme.presentation.ui.screens.login.LoginScreen
import com.atitienei_daniel.reeme.presentation.ui.screens.on_boarding.OnBoardingScreen
import com.atitienei_daniel.reeme.presentation.ui.screens.register.RegisterScreen
import com.atitienei_daniel.reeme.presentation.ui.utils.Screens
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun Navigation() {
    val navController = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = Screens.OnBoardingScreen.route
    ) {
        composable(
            route = Screens.OnBoardingScreen.route,
            enterTransition = {
                slideInVertically(initialOffsetY = { 1000 }, animationSpec = tween(500)) +
                        fadeIn()
            },
            popEnterTransition = {
                slideInVertically(initialOffsetY = { 1000 }, animationSpec = tween(500)) +
                        fadeIn()
            },
            exitTransition = {
                slideOutVertically(targetOffsetY = { 1000 }, animationSpec = tween(500)) +
                        fadeOut()
            },
            popExitTransition = {
                slideOutVertically(targetOffsetY = { 1000 }, animationSpec = tween(500)) +
                        fadeOut()
            }
        ) {
            OnBoardingScreen(navController = navController)
        }

        composable(
            route = Screens.Register.route,
            enterTransition = {
                fadeIn(animationSpec = tween(700))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(700))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(700))
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
                fadeIn(animationSpec = tween(700))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(700))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(700))
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(700))
            }
        ) {
            LoginScreen(navController = navController)
        }
    }
}
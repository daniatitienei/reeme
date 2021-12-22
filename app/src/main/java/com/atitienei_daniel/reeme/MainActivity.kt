package com.atitienei_daniel.reeme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.atitienei_daniel.reeme.presentation.theme.ReemeTheme
import com.atitienei_daniel.reeme.presentation.ui.screens.on_boarding.OnBoardingScreen
import com.atitienei_daniel.reeme.presentation.ui.screens.register.RegisterScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReemeTheme {
                val systemUiController = rememberSystemUiController()

                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = Color.White,
                        darkIcons = true
                    )
                }

                RegisterScreen()
            }
        }
    }
}

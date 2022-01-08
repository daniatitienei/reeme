package com.atitienei_daniel.reeme

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.atitienei_daniel.reeme.domain.repository.StoreThemeRepository
import com.atitienei_daniel.reeme.ui.theme.ReemeTheme
import com.atitienei_daniel.reeme.ui.utils.enums.Theme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.squareup.moshi.Moshi
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var repository: StoreThemeRepository

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        createChannel(
            name = "Reminders",
            description = "",
            channelId = "reminder"
        )

        setContent {
            val currentTheme = repository.getTheme.collectAsState(initial = Theme.AUTO).value

            val systemTheme = isSystemInDarkTheme()

            ReemeTheme(
                isDarkTheme = when (currentTheme) {
                    Theme.AUTO -> systemTheme
                    Theme.LIGHT -> false
                    Theme.DARK -> true
                }
            ) {
                Navigation(repository = repository)
            }
        }
    }

    private fun createChannel(name: CharSequence, description: String, channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                this.description = description
            }
            val notificationMananger = getSystemService(
                NotificationManager::class.java
            )

            notificationMananger.createNotificationChannel(channel)
        }
    }
}

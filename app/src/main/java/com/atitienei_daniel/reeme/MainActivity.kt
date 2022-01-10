package com.atitienei_daniel.reeme

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.atitienei_daniel.reeme.data.reminders_db.RemindersDataSource
import com.atitienei_daniel.reeme.domain.repository.StoreThemeRepository
import com.atitienei_daniel.reeme.ui.theme.ReemeTheme
import com.atitienei_daniel.reeme.ui.utils.enums.Theme
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var repository: StoreThemeRepository

    @Inject
    lateinit var remindersDatasource: RemindersDataSource

    var mInterstitialAd: InterstitialAd? = null
    var remindersSize: Int? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        createChannel(
            name = "Reminders",
            channelId = "reminder"
        )

        GlobalScope.launch {
            remindersDatasource.getReminders().collect {
                remindersSize = it.size
            }
        }

        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this@MainActivity,
            getString(R.string.interstitial_ad),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.message)
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded.")
                    mInterstitialAd = interstitialAd

                    if (remindersSize!! % 2 != 0)
                        mInterstitialAd?.show(this@MainActivity)
                }
            }
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

    private fun createChannel(name: CharSequence, description: String = "", channelId: String) {
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

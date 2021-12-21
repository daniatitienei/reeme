package com.atitienei_daniel.reeme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.atitienei_daniel.reeme.presentation.theme.ReemeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReemeTheme {

            }
        }
    }
}

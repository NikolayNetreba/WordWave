package com.example.wordwave

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.wordwave.ui.theme.WordWaveTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WordWaveTheme{
                AllApp()

            }
        }
    }
}

@Composable
fun AllApp() {
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color(0xFF507ba0),
            darkIcons = true
        )
        systemUiController.setNavigationBarColor(
            color = Color.White,
            darkIcons = true
        )
    }
    VocabularyScreen()
}
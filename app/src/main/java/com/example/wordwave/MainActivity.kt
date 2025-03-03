package com.example.wordwave

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
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
//    val systemUiController = rememberSystemUiController()
//    systemUiController.setStatusBarColor(Color.Transparent, darkIcons = true)
//    systemUiController.setNavigationBarColor(Color.Transparent, darkIcons = true)
    HomePageScreen()
}
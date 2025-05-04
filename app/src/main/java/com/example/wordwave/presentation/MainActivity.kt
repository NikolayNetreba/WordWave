package com.example.wordwave.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wordwave.R
import com.example.wordwave.presentation.db.DictionaryViewModel
import com.example.wordwave.presentation.ui.theme.WordWaveTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    private val viewModel: DictionaryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WordWaveTheme {
                AllApp(viewModel)
            }
        }
    }
}

@Composable
fun AllApp(viewModel: DictionaryViewModel) {
    val navController = rememberNavController()
    val color = colorResource(R.color.bar)
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = color, darkIcons = true)
        systemUiController.setNavigationBarColor(color = Color.White, darkIcons = true)
    }

    NavHost(navController = navController, startDestination = "home_screen") {
        composable("home_screen") { HomePageScreen(navController) }
        composable("vocabulary_screen") { VocabularyScreen(navController) }
        composable("add_word_screen") { AddWordScreen(navController, viewModel) }
        composable("translate_screen") { TranslateScreen(navController) }
    }
}

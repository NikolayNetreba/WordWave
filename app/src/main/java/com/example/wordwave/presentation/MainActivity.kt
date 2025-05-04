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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wordwave.LibreTranslateApi
import com.example.wordwave.R
import com.example.wordwave.presentation.db.DictionaryViewModel
import com.example.wordwave.TranslationViewModel
import com.example.wordwave.presentation.ui.theme.WordWaveTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue

class MainActivity : ComponentActivity() {
    private val DviewModel: DictionaryViewModel by viewModels()
    private val TviewModel: TranslationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WordWaveTheme {
                AllApp(DviewModel, TviewModel)
            }
        }
    }
}

@Composable

fun AllApp(DviewModel: DictionaryViewModel, TviewModel: TranslationViewModel) {

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
        composable("add_word_screen") { AddWordScreen(navController, DviewModel) }
        composable("translate_screen") { TranslateScreen(navController, TviewModel) }
    }
}

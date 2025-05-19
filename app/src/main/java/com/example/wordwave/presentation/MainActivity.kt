package com.example.wordwave.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wordwave.R
import com.example.wordwave.presentation.ui.theme.WordWaveTheme
import com.example.wordwave.presentation.game.FlashCardsScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlin.getValue

class MainActivity : ComponentActivity() {

    private val FCviewModel: FlashCardsViewModel by viewModels()
    private val DviewModel: DictionaryViewModel by viewModels()
    private val TviewModel: TranslationViewModel by viewModels()
    private val FakeViewModel: FakeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            WordWaveTheme {
                AllApp(DviewModel, TviewModel, FCviewModel, FakeViewModel)
            }
        }
    }
}

@Composable
fun AllApp(
    DviewModel: DictionaryViewModel,
    TviewModel: TranslationViewModel,
    FCviewModel: FlashCardsViewModel,
    FakeViewModel: FakeViewModel
) {

    val navController = rememberNavController()
    val color = colorResource(R.color.bar)
    val systemUiController = rememberSystemUiController()

    FCviewModel.navController = navController
    FCviewModel.dictionaryViewModel = DviewModel

    DviewModel.initialize()

    SideEffect {
        systemUiController.setStatusBarColor(color = color, darkIcons = true)
        systemUiController.setNavigationBarColor(color = Color.White, darkIcons = true)
    }

    NavHost(navController = navController, startDestination = "home_screen") {
        composable("home_screen") { HomePageScreen(navController, FCviewModel, DviewModel) }
        composable("vocabulary_screen") { VocabularyScreen(navController, DviewModel) }
        composable("add_word_screen") { AddWordScreen(navController, DviewModel, TviewModel) }
        composable("translate_screen") { TranslateScreen(navController, TviewModel) }
        composable("flash_cards_screen") { FlashCardsScreen(navController, FCviewModel) }
        composable ("show_card_screen") { ShowCardScreen(navController, DviewModel) }
    }
}

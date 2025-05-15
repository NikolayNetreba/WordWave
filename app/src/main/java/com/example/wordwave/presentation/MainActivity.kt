package com.example.wordwave.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
<<<<<<< Updated upstream
=======
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
>>>>>>> Stashed changes
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wordwave.R
import com.example.wordwave.presentation.db.DictionaryViewModel
import com.example.wordwave.presentation.ui.theme.WordWaveTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
<<<<<<< Updated upstream
    private val viewModel: DictionaryViewModel by viewModels()
=======
    private val DviewModel: DictionaryViewModel by viewModels()
    private val TviewModel: TranslationViewModel by viewModels()
    private val CardGameViewModel: CardGameViewModel by viewModels()
>>>>>>> Stashed changes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WordWaveTheme {
<<<<<<< Updated upstream
                AllApp(viewModel)
=======
                AllApp(DviewModel, TviewModel, CardGameViewModel)
>>>>>>> Stashed changes
            }
        }
    }
}

@Composable
<<<<<<< Updated upstream
fun AllApp(viewModel: DictionaryViewModel) {
=======
fun AllApp(DviewModel: DictionaryViewModel, TviewModel: TranslationViewModel, CardGameViewModel: CardGameViewModel) {

>>>>>>> Stashed changes
    val navController = rememberNavController()
    val color = colorResource(R.color.bar)
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = color, darkIcons = true)
        systemUiController.setNavigationBarColor(color = Color.White, darkIcons = true)
    }

    NavHost(navController = navController, startDestination = "card_game") {
        composable("card_game") { CardGameScreen(CardGameViewModel) }
        composable("home_screen") { HomePageScreen(navController) }
        composable("vocabulary_screen") { VocabularyScreen(navController, viewModel) }
        composable("add_word_screen") { AddWordScreen(navController, viewModel  ) }
        composable("translate_screen") { TranslateScreen(navController) }
    }
}

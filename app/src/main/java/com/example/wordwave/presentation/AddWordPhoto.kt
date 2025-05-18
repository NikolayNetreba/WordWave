package com.example.wordwave.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.wordwave.data.local.db.OCRCache
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWordPhotoScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var texts by remember { mutableStateOf<List<String>>(emptyList()) }
    val tViewModel: TranslationViewModel = viewModel()

    // Загрузка кэша при старте
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            texts = OCRCache.getAllTexts(context)
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Снимки экрана") }) }
    ) { padding ->
        if (texts.isEmpty()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("Нет сохраненных текстов")
            }
        } else {
            LazyColumn(
                contentPadding = padding,
                modifier = Modifier.fillMaxSize()
            ) {
                items(texts) { text ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                // Передать текст в поле ввода и перейти
                                tViewModel.updateInputText(text)
                                // Очищаем кэш выбранного текста
                                coroutineScope.launch { OCRCache.clearText(context) }
                                navController.navigate("add_word_screen")
                            },
                        shape = MaterialTheme.shapes.medium,
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Text(
                            text = text,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
private fun PreviewAddWord() {
    val navController = rememberNavController()
    AddWordPhotoScreen(navController)
}
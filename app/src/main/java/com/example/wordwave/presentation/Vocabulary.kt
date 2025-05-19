package com.example.wordwave.presentation

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.wordwave.R
import com.example.wordwave.data.local.db.WordWithTranslations
import java.util.Locale
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.animation.animateColorAsState
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.getValue

@Composable
fun VocabularyScreen(navController: NavHostController, viewModel: DictionaryViewModel) {
    /*val words: List<Pair<String, String>> = listOf(
        "hi" to "Привет",
        "bye" to "Пока",
        "apple" to "Яблоко",
        "table" to "Стол",
        "cat" to "Кот"
    )*/

    val categories = listOf("Все", "Изучаемые", "Выученные", "Фразы", "Слова")
    viewModel.updateWords(1)
    Scaffold(
        topBar = {
            Column {
                VocabularyTopBar()
                CategoryTabs(categories, navController)
            }
        },
        bottomBar = {
            NavigationBar(navController)
        },
        modifier = Modifier.padding(WindowInsets.navigationBars.asPaddingValues()),
        containerColor = Color.White,
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color.White)
            ) {
                viewModel.updateLanguages("u0")
                viewModel.updateWordsWithTranslations("en")
                ShowWordList(viewModel.wordsWithTranslations, navController, viewModel)
            }
        }
    )

}

@Composable
fun CategoryTabs(categories: List<String>, navController: NavHostController) {
    val selectedCategoryIndex = remember { mutableStateOf(0) }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.bar))
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        itemsIndexed(categories) { index, category ->
            CategoryTab(
                category = category,
                isSelected = index == selectedCategoryIndex.value,
                onClick = { selectedCategoryIndex.value = index },
                navController
            )
        }
    }
}

@Composable
fun CategoryTab(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    navController: NavHostController
) {
    Text(
        text = category,
        fontSize = 16.sp,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
        color = if (isSelected) Color.White else colorResource(R.color.text_bar),
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 3.dp)
            .clickable { onClick() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VocabularyTopBar() {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.vocabulary_title),
                    fontSize = dimensionResource(R.dimen.title_size).value.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = {}, modifier = Modifier.padding(horizontal = 2.dp)) {
                Icon(
                    painterResource(R.drawable.profile_icon),
                    contentDescription = "Профиль",
                    tint = Color.White
                )
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    painterResource(R.drawable.search),
                    contentDescription = "Поиск",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(R.color.bar)
        )
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShowWordList(
    words: List<WordWithTranslations>,
    navController: NavHostController,
    viewModel: DictionaryViewModel
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(
            items = words,
            key = { _, wwt -> wwt.word.id }
        ) { index, wwt ->
            // 1) Запоминаем состояние свайпа
            val dismissState = rememberDismissState { dismissValue ->
                if (dismissValue == DismissValue.DismissedToStart) {
                    // свайп влево завершён — удалим слово
                    viewModel.deleteWord(wwt.word)
                    true
                } else {
                    false
                }
            }

            // 2) SwipeToDismiss из androidx.compose.material
            SwipeToDismiss(
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart),
                background = {
                    val bgColor by animateColorAsState(
                        targetValue = if (dismissState.targetValue == DismissValue.Default)
                            Color.LightGray else Color.Red
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(bgColor)
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Удалить",
                            tint = Color.White
                        )
                    }
                },
                dismissContent = {
                    // ваш NoteItem
                    Column {
                        NoteItem(
                            word         = wwt.word.text,
                            translation  = wwt.translations
                                .firstOrNull()
                                ?.translation
                                ?.translatedText
                                .orEmpty(),
                            isFirst      = index == 0,
                            navController = navController,
                            wwt           = wwt,
                            viewModel     = viewModel
                        )
                        Divider(color = Color.LightGray, thickness = 1.dp)
                    }
                }
            )
        }
    }
}


@Composable
fun NoteItem(
    word: String,
    translation: String,
    isFirst: Boolean,
    navController: NavHostController,
    wwt: WordWithTranslations,
    viewModel: DictionaryViewModel
) {
    val context = LocalContext.current

    // 1) Создаём и запоминаем экземпляр TTS
    val tts = remember {
        // временная переменная, чтобы её было видно в OnInitListener
        var ttsTemp: TextToSpeech? = null
        ttsTemp = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // как только TTS готов — задаём язык
                ttsTemp?.language = Locale("en")  // используйте Locale("ru") для русского
            }
        }
        ttsTemp!!
    }

    // 2) Освобождаем ресурсы TTS, когда этот Composable уходит из экрана
    DisposableEffect(tts) {
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = if (isFirst)
                    RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                else
                    RoundedCornerShape(0.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                navController.navigate("show_card_screen")
                viewModel.cr = wwt
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = word, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Text(text = translation, fontSize = 14.sp, color = Color.Gray)
        }

        IconButton(onClick = {
            // 3) При каждом нажатии говорим слово
            tts.speak(
                word,
                TextToSpeech.QUEUE_FLUSH,
                /* params = */ null,
                /* utteranceId = */ "NoteItemUtterance"
            )
        }) {
            Icon(
                painter = painterResource(R.drawable.volium),
                contentDescription = "Произношение",
                tint = Color.Black
            )
        }
    }
}

/*@Preview(showSystemUi = true)
@Composable
fun VocabularyPreview() {
    val navController = rememberNavController()
    VocabularyScreen(navController)
}*/
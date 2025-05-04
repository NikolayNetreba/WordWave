package com.example.wordwave.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.content.contentReceiver
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.wordwave.R
import com.example.wordwave.TranslationViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TranslateScreen(
    navController: NavHostController,
    viewModel: TranslationViewModel
) {
    Scaffold(
        topBar = {
            Column {
                TopBar(navController)
                HorizontalDivider(thickness = 1.dp, color = colorResource(R.color.line))
            }
        },
        bottomBar = { NavigationBar(navController) },
        containerColor = Color.White,
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .padding(top = 10.dp)
                    .navigationBarsPadding()
            ) {
                Box(
                    modifier = Modifier.background(
                        colorResource(id = R.color.grey_graph),
                        shape = RoundedCornerShape(12.dp)
                    )
                ) {
                    Column {
                        LanguageSelector()
                        HorizontalDivider(thickness = 1.dp, color = colorResource(R.color.line))
                        TranslateInputField(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navController: NavHostController) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.translate_title),
                fontSize = dimensionResource(R.dimen.title_size).value.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        },
        navigationIcon = {
            IconButton(onClick = {}, modifier = Modifier.padding(horizontal = 2.dp)) {
                Icon(
                    painterResource(R.drawable.profile_icon),
                    contentDescription = "profile_icon",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = colorResource(R.color.bar)
        )
    )
}


@Composable
private fun LanguageSelector() {
    val (sourceLanguage, setSourceLanguage) = remember { mutableStateOf("Английский") }
    val (targetLanguage, setTargetLanguage) = remember { mutableStateOf("Русский") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black
            ),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = sourceLanguage,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Clip,
            )
        }

        IconButton(
            onClick = {
                val temp = sourceLanguage
                setSourceLanguage(targetLanguage)
                setTargetLanguage(temp)
            },
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Icon(
                painterResource(R.drawable.swap_languages),
                contentDescription = "swap languages",
                tint = Color.Black
            )
        }

        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black
            ),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = targetLanguage,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Clip,
            )
        }
    }
}

@Composable
fun TranslateInputField(
    viewModel: TranslationViewModel, // Callback при изменении текста
    navController: NavHostController      // Контроллер навигации
) {
    val inputText by viewModel.inputText.collectAsState()
    val translatedText by viewModel.translatedText.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    // Локальное состояние для текста
    var localInputText by remember { mutableStateOf(inputText) }

    // Рассчитываем размер шрифта
    val fontSize = with(LocalDensity.current) {
        when {
            localInputText.length < 30 -> 18.sp
            localInputText.length < 60 -> 16.sp
            else -> 14.sp
        }
    }

    // Состояние прокрутки
    val scrollState = rememberScrollState()

    LaunchedEffect(inputText) {
        if (inputText.isNotBlank()) viewModel.translateText()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.grey_graph))
            .clip(RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Секция ввода текста
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .heightIn(min = 50.dp, max = 150.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Поле ввода
                Box(modifier = Modifier.weight(1f)) {
                    TextField(
                        value = inputText,
                        onValueChange = viewModel::updateInputText,
                        placeholder = { Text("Введите текст") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Gray,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            cursorColor = Color.Black,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        textStyle = LocalTextStyle.current.copy(fontSize = fontSize),
                        singleLine = false
                    )
                }

                // Кнопка очистки
                if (localInputText.isNotEmpty()) {
                    IconButton(
                        onClick = { localInputText = "" },
                        modifier = Modifier.align(Alignment.Top)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.add_2_24),
                            contentDescription = "Clear text",
                            tint = Color.Black,
                            modifier = Modifier.rotate(45f)
                        )
                    }
                }
            }

            // Секция перевода (только если есть текст)
            if (localInputText.isNotEmpty()) {
                // Кнопки действий
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { /* Прослушивание */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.volium),
                            contentDescription = "Play text",
                            tint = Color.Black
                        )
                    }
                    IconButton(onClick = { navController.navigate("add_word_screen") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.add_2_24),
                            contentDescription = "Add word",
                            tint = Color.Black
                        )
                    }
                }

                // Разделитель
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = colorResource(id = R.color.line)
                )

                // Блок с переводом
                Box(
                    modifier = Modifier
                        .heightIn(max = 150.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .verticalScroll(scrollState)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        } else {
                            Text(
                                text = translatedText,
                                fontSize = fontSize,
                                color = Color.Black,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
                }

                // Кнопка прослушивания перевода
                IconButton(onClick = { /* Прослушивание перевода */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.volium),
                        contentDescription = "Play translation",
                        tint = Color.Black
                    )
                }
            }
        }
    }
}


@Composable
fun AdditionalTranslations(translations: List<String>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        translations.forEach { translation ->
            Text(
                text = translation,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
            )
        }
    }
}


@Composable
private fun TranslationHistory() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
        ) {
            Text(
                text = stringResource(R.string.translation_history),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Icon(
                painterResource(id = R.drawable.arrow_down),
                contentDescription = "Show More",
                tint = Color.Black,
            )
        }
        LazyColumn {
            items(5) {
                TranslationHistoryItem(wordPair = Pair("Word", "Слово"))
            }
        }
    }
}


@Composable
private fun TranslationHistoryItem(wordPair: Pair<String, String>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(colorResource(id = R.color.grey_graph), shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = wordPair.first,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = wordPair.second,
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true)
private fun PreviewTranslate() {
    val navController = rememberNavController()
    val viewModel: TranslationViewModel = viewModel()
    TranslateScreen(navController, viewModel)
}

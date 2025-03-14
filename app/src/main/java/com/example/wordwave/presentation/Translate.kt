package com.example.wordwave.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.content.contentReceiver
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

@Composable
fun TranslateScreen(navController: NavHostController) {
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
            ) {
                Box(
                    modifier = Modifier.background(colorResource(id = R.color.grey_graph), shape = RoundedCornerShape(12.dp))
                ) {
                    Column {
                        LanguageSelector()
                        HorizontalDivider(thickness = 1.dp, color = colorResource(R.color.line))
                        TranslateInputField()
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                TranslationHistory()
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
            contentPadding = PaddingValues(0.dp),
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
fun TranslateInputField() {
    var inputText by remember { mutableStateOf("") }
    var translatedText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(color = colorResource(R.color.grey_graph), shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        // Поле ввода текста
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = {
                    Text(
                        text = stringResource(R.string.enter_text),
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp),
                singleLine = true,
            )


            // Кнопка очистки текста (крестик)
            if (inputText.isNotEmpty()) {
                IconButton(onClick = { inputText = "" }) {
                    Icon(
                        painterResource(R.drawable.close_icon),
                        contentDescription = "Clear text",
                        tint = Color.Black
                    )
                }
            }

            // Кнопка прослушивания текста
            IconButton(onClick = { /* Реализуйте функционал прослушивания */ }) {
                Icon(
                    painterResource(id = R.drawable.volium),
                    contentDescription = "Play text",
                    tint = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Поле отображения перевода
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = translatedText,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )

            // Кнопка прослушивания перевода
            IconButton(onClick = { /* Реализуйте функционал прослушивания перевода */ }) {
                Icon(
                    painterResource(id = R.drawable.volium),
                    contentDescription = "Play translation",
                    tint = Color.Black
                )
            }
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
    TranslateScreen(navController)
}

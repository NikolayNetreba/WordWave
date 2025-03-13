package com.example.wordwave.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
            ) {
                LanguageSelector()
                InputField()
                TranslationHistory(modifier = Modifier.padding(top = 24.dp))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navController: NavHostController) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.add_word_title),
                    fontSize = dimensionResource(R.dimen.title_size).value.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.padding(horizontal = 2.dp)) {
                Icon(
                    painterResource(R.drawable.backbutton),
                    contentDescription = "back",
                    tint = Color.Black
                )
            }
        },
        actions = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painterResource(R.drawable.tick),
                    contentDescription = "user",
                    tint = Color.Black
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
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
            onClick = { /* TODO: Implement language selection logic */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Black),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = sourceLanguage,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black,
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
            onClick = { /* TODO: Implement language selection logic */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Black),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = targetLanguage,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black,
            )
        }
    }
}

@Composable
private fun InputField() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(vertical = 8.dp)
            .background(colorResource(id = R.color.grey_graph), shape = RoundedCornerShape(8.dp))

    ) {
        Text(
            text = stringResource(id = R.string.enter_text),
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier
                .padding(8.dp)
        )
    }
}

@Composable
private fun TranslationHistory(modifier: Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.translation_history),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painterResource(id = R.drawable.arrow_down),
                contentDescription = "Show More",
                tint = Color.Black,
                modifier = Modifier.clickable {
                    /* TODO: Implement show more logic */
                }
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
            .padding(16.dp)
    ) {
        Column (
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

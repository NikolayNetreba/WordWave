package com.example.wordwave

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@Composable
fun VocabularyScreen(navController: NavController) {
    val words: List<Pair<String, String>> = listOf(
        "hi" to "Привет",
        "bye" to "Пока",
        "apple" to "Яблоко",
        "table" to "Стол",
        "cat" to "Кот"
    )

    val categories = listOf("Все", "Изучаемые", "Выученные", "Фразы", "Слова")

    Scaffold(
        topBar = { Column {
            VocabularyTopBar()
            CategoryTabs(categories)
        }},
        bottomBar = {
            NavigationBar{
                navController.navigate("home_screen")
            }
        },
        containerColor = Color.White,
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color.White)
            ) {
                ShowWordList(words)
            }
        }
    )

}

@Composable
fun CategoryTabs(categories: List<String>) {
    var selectedCategoryIndex = remember { mutableStateOf(0) }

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
                onClick = { selectedCategoryIndex.value = index }
            )
        }
    }
}

@Composable
fun CategoryTab(category: String, isSelected: Boolean, onClick: () -> Unit) {
    Text(
        text = category,
        fontSize = 16.sp,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
        color = if (isSelected) Color.White else colorResource(R.color.text_bar),
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 3.dp)
            .clickable {onClick()}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabularyTopBar() {
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

@Composable
fun ShowWordList(words: List<Pair<String, String>>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(words) { index, (word, translation) ->
            NoteItem(
                word = word,
                translation = translation,
                isFirst = index == 0
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = colorResource(R.color.line)
            )

        }
    }
}

@Composable
fun NoteItem(word: String, translation: String, isFirst: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = if (isFirst) RoundedCornerShape(
                    topStart = 10.dp,
                    topEnd = 10.dp
                ) else RoundedCornerShape(0.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = word, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Text(text = translation, fontSize = 14.sp, color = Color.Gray)
        }
        IconButton(onClick = {}) {
            Icon(painterResource(R.drawable.volium), contentDescription = "Произношение")
        }
    }
}









package com.example.wordwave

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@Composable
fun VocabularyScreen() {
    val notes: List<Pair<String, String>> = listOf(
        "hi" to "Привет",
        "hi" to "Привет",
        "hi" to "Привет",
        "hi" to "Привет",
        "hi" to "Привет",
        "hi" to "Привет",
        "hi" to "Привет",
        "hi" to "Привет",
        "hi" to "Привет",
        "hi" to "Привет",
        "hi" to "Привет",
        "hi" to "Привет",
        "hi" to "Привет",
        "hi" to "Привет",
        "hi" to "Привет",
        "hi" to "Привет",
        "hi" to "Привет",
        "hi" to "Привет",
        "hi" to "Привет",
        "hi" to "Привет",
        "hi" to "Привет",
        "hi" to "Привет",
        )
    val padding_10 = dimensionResource(R.dimen.padding_10)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Box(
            modifier = Modifier.background(colorResource(R.color.bar))
        ) {
            Bar()
        }
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            ShowWordList(notes)
        }
        HorizontalDivider(thickness = 2.dp, color = colorResource(R.color.line))
        Box(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
        ) {
            NavigationBar()
        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun VocabularyPreview() {
    VocabularyScreen()

}

@Composable
fun Bar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.padding_10))
    ) {
        IconButton(
            modifier = Modifier.align(Alignment.CenterStart),
            onClick = {}
        ) {
            Icon(painterResource(R.drawable.profile_icon), contentDescription = "profile_icon", tint = Color.White)
        }
        IconButton(
            onClick = {},
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(painterResource(R.drawable.search), contentDescription = "search", tint = Color.White)
        }
        Text(
            text = stringResource(R.string.vocabulary_title),
            fontSize = dimensionResource(R.dimen.title_size).value.sp,
            modifier = Modifier.align(Alignment.Center),
            color = Color.White
        )
    }
}

@Composable
fun ShowWordList(notes: List<Pair<String, String>>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(notes) { index, (word, translation) ->
            NoteItem(
                word = word,
                translation = translation,
                isFirst = index == 0
            )
            if (index < notes.size - 1) {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = colorResource(R.color.line)
                )
            }
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
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = word, fontSize = 18.sp)
            Text(text = translation, fontSize = 14.sp, color = Color.Gray)
        }
        IconButton(onClick = {}) {
            Icon(painterResource(R.drawable.volium), contentDescription = "volium")
        }
    }
}








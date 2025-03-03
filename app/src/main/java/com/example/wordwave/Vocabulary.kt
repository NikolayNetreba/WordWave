package com.example.wordwave

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Bar()
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
            .padding(dimensionResource(R.dimen.padding_10))
    ) {
        IconButton(
            modifier = Modifier.align(Alignment.CenterStart),
            onClick = {}
        ) {
            Image(painterResource(R.drawable.profile_icon), contentDescription = "profile_icon")
        }
        IconButton(
            onClick = {},
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Image(painterResource(R.drawable.search), contentDescription = "search")
        }
        Text(
            text = stringResource(R.string.vocabulary_title),
            fontSize = dimensionResource(R.dimen.title_size).value.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}








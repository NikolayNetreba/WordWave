package com.example.wordwave

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Preview(showSystemUi = true)
@Composable
fun HomePageScreen() {
    val bigPadding = dimensionResource(R.dimen.padding_big)
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(color = Color.White)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Bar(stringResource(R.string.bar_title))
        HorizontalDivider(thickness = 2.dp, color = colorResource(R.color.line))
        AllWordButton()
        HorizontalDivider(
            thickness = 2.dp,
            color = colorResource(R.color.line),
            modifier = Modifier.padding(start = bigPadding, end = bigPadding)
        )
        Diagrams()
        HorizontalDivider(
            thickness = 2.dp,
            color = colorResource(R.color.line),
            modifier = Modifier.padding(start = bigPadding, end = bigPadding)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            GamesTable()
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


@Composable
private fun Bar(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            fontSize = dimensionResource(R.dimen.title_size).value.sp,
            modifier = Modifier.align(Alignment.Center),
            fontWeight = FontWeight.SemiBold
        )
        val context = LocalContext.current

        IconButton(
            onClick = { Toast.makeText(context, "HI", Toast.LENGTH_SHORT).show() },
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = dimensionResource(R.dimen.padding_10))
        ) {
            Image(
                painter = painterResource(R.drawable.profile_icon),
                contentDescription = "icon"
            )
        }
    }

}

@Composable
fun AllWordButton() {

    Box(
        modifier = Modifier
            .padding(dimensionResource(R.dimen.padding_large))
            .fillMaxWidth()
            .height(45.dp)
            .clip(RoundedCornerShape(dimensionResource(R.dimen.round_shape)))
            .background(colorResource(R.color.grey_graph))
            .clickable {

            }
    ) {
        Text(
            text = stringResource(R.string.all_word),
            fontSize = dimensionResource(R.dimen.main_text).value.sp,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = dimensionResource(R.dimen.padding_15)),
            fontWeight = FontWeight.Medium
        )

        Image(
            painterResource(R.drawable.baseline_arrow_forward_ios_24),
            contentDescription = " ",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(
                    end = dimensionResource(R.dimen.padding_15)
                )
        )

    }
}

@Composable
fun Diagrams() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .padding(dimensionResource(R.dimen.padding_large))
            .clip(RoundedCornerShape(dimensionResource(R.dimen.round_shape)))
            .background(colorResource(R.color.grey_graph))
    ) {

    }
}

@Composable
fun GamesTable() {
    Box(
        modifier = Modifier
            .padding(dimensionResource(R.dimen.padding_large))
            .fillMaxSize()
            .clip(RoundedCornerShape(dimensionResource(R.dimen.round_shape)))
            .background(colorResource(R.color.grey_graph))
    ) {
        Text(
            text = stringResource(R.string.game_table_title),
            fontSize = dimensionResource(R.dimen.title_size).value.sp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(10.dp)
        )
    }
}

@Composable
fun NavigationBar() {
    Box(
        modifier = Modifier
            .padding(top = 5.dp, start = 40.dp, end = 40.dp)
            .fillMaxWidth()
            .height(42.dp)
            .background(Color.White)
    ) {
        IconButton(
            onClick = {},
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(painterResource(R.drawable.home_ic), contentDescription = "Home_ic")
        }
        IconButton(
            onClick = {},
            modifier = Modifier.align(Alignment.Center)
        ) {
            Icon(painterResource(R.drawable.add_button), contentDescription = "add_button")
        }
        IconButton(
            onClick = {},
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(painterResource(R.drawable.translate_ic), contentDescription = "translate_ic")
        }
    }
}



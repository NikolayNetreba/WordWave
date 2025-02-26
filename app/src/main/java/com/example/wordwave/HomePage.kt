package com.example.wordwave

import android.icu.text.CaseMap.Title
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showSystemUi = true)
@Composable
fun HomePageScreen() {
    val bigPadding = dimensionResource(R.dimen.padding_big)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Bar(stringResource(R.string.bar_title))
        HorizontalDivider(
            thickness = 2.dp,
            color = colorResource(R.color.line)
        )
        Diagrams()
        HorizontalDivider(
            thickness = 2.dp,
            color = colorResource(R.color.line),
            modifier = Modifier.padding(start = bigPadding, end = bigPadding)
        )
    }
}


@Composable
fun Bar(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = dimensionResource(R.dimen.padding_large))
    ) {
        Text(
            text = title,
            fontSize = dimensionResource(R.dimen.title_size).value.sp,
            modifier = Modifier.align(Alignment.Center)
        )
        val context = LocalContext.current

        IconButton(
            onClick = { Toast.makeText(context, "HI", Toast.LENGTH_SHORT).show() },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Image(
                painter = painterResource(R.drawable.profile_icon),
                contentDescription = "icon",
            )
        }
    }

}

@Composable
fun Diagrams(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .padding(dimensionResource(R.dimen.padding_large))
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(13.dp))
                .fillMaxSize()
                .background(colorResource(R.color.grey_graph))
        ) {

        }
    }
}


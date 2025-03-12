package com.example.wordwave.presentation

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.wordwave.R

@Composable
fun HomePageScreen(navController: NavHostController) {
    Scaffold(
        topBar = { Bar() },
        bottomBar = { NavigationBar{
            navController.navigate("home_screen")
        } },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color.White)
            ) {
                AllWordButton{
                    navController.navigate("vocabulary_screen")
                }
                HorizontalDivider(color = colorResource(R.color.line), thickness = 2.dp)
                Diagrams()
                HorizontalDivider(color = colorResource(R.color.line), thickness = 2.dp)
                GamesTable()
            }
        },
        containerColor = Color.White
    )
}

@Composable
private fun Bar() {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.bar))
            .statusBarsPadding()
    ) {
        Text(
            text = stringResource(R.string.bar_title),
            fontSize = dimensionResource(R.dimen.title_size).value.sp,
            modifier = Modifier.align(Alignment.Center),
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )

        IconButton(
            onClick = { Toast.makeText(context, "HI", Toast.LENGTH_SHORT).show() },
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(vertical = 8.dp, horizontal = 6.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.profile_icon),
                contentDescription = "icon",
                tint = Color.White
            )
        }
    }

}


@Composable
fun AllWordButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(45.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(colorResource(R.color.grey_graph))
            .clickable { onClick() }
    ) {
        Text(
            text = stringResource(R.string.all_word),
            fontSize = 18.sp,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp),
            fontWeight = FontWeight.Medium
        )
        Icon(
            painterResource(R.drawable.baseline_arrow_forward_ios_24),
            contentDescription = "Arrow",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
        )
    }
}

@Composable
fun Diagrams() {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(260.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(colorResource(R.color.grey_graph))
    ) {}
}

@Composable
fun GamesTable() {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .clip(RoundedCornerShape(12.dp))
            .background(colorResource(R.color.grey_graph))
    ) {
        Text(
            text = stringResource(R.string.game_table_title),
            fontSize = 20.sp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(10.dp)
        )
    }
}

@Composable
fun NavigationBar(onClick: () -> Unit) {
    Column(
        modifier = Modifier.statusBarsPadding()
    ) {
        HorizontalDivider(color = colorResource(R.color.line), thickness = 2.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.White),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick) {
                Icon(
                    painterResource(R.drawable.home_ic),
                    contentDescription = "Home"
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    painterResource(R.drawable.add_button),
                    contentDescription = "Add"
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    painterResource(R.drawable.translate_ic),
                    contentDescription = "Translate"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePageScreenPreview() {
    val navController = rememberNavController()
    HomePageScreen(navController = navController)
}

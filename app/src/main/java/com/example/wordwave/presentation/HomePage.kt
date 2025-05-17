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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.wordwave.R

@Composable
fun HomePageScreen(navController: NavHostController) {
    Scaffold(
        topBar = { TopBar(navController, "Английский") },
        bottomBar = { NavigationBar(navController)},
        modifier = Modifier.padding(WindowInsets.navigationBars.asPaddingValues()),
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color.White)
            ) {
                AllWordButton(navController)
                HorizontalDivider(color = colorResource(R.color.line), thickness = 1.dp, modifier = Modifier.padding(horizontal = 32.dp))
                Diagrams()
                HorizontalDivider(color = colorResource(R.color.line), thickness = 1.dp, modifier = Modifier.padding(horizontal = 32.dp))
                GamesTable(navController)
            }
        },
        containerColor = Color.White
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TopBar(navController: NavHostController, title: String) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontSize = dimensionResource(R.dimen.title_size).value.sp,
                fontWeight = FontWeight.SemiBold,

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
            containerColor = colorResource(R.color.bar),
            titleContentColor = Color.White
        )
    )
}


@Composable
fun AllWordButton(navController: NavHostController) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(45.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(colorResource(R.color.grey_graph))
            .clickable { navController.navigate("vocabulary_screen")}
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
                .padding(end = 16.dp),
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
fun GamesTable(navController: NavHostController) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.game_table_title),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        val games = listOf(
            Triple("Флеш-карточки", R.drawable.flash_cards_icon, "flash_cards_screen"),
            Triple("Выбрать слово", R.drawable.flash_cards_icon, "choose_word_screen"),
            Triple("Собрать слово", R.drawable.flash_cards_icon, "build_word_screen"),
            Triple("Найти пары", R.drawable.flash_cards_icon, "find_pairs_screen")
        )

        games.forEach { (title, iconRes, route) ->
            GameButton(title = title, iconRes = iconRes) {
                navController.navigate(route)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun GameButton(title: String, iconRes: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(colorResource(R.color.grey_graph))
            .clickable { onClick() }
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
        )
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = title,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            tint = colorResource(R.color.purple_I)
        )
    }
}



@Composable
fun NavigationBar(navController: NavHostController) {
    Column(
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
            IconButton(onClick = {navController.navigate("home_screen")}) {
                Icon(
                    painterResource(R.drawable.home_ic),
                    contentDescription = "Home"
                )
            }
            IconButton(onClick = {navController.navigate("add_word_screen")}) {
                Icon(
                    painterResource(R.drawable.add_button),
                    contentDescription = "Add"
                )
            }
            IconButton(onClick = {navController.navigate("translate_screen")}) {
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

package com.example.wordwave.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
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
import androidx.navigation.compose.rememberNavController
import com.example.wordwave.R

@Composable
fun AddWordScreen(navController: NavController) {
    Scaffold(
        topBar = {
            Column {
                TopBar()
                HorizontalDivider(thickness = 1.dp, color = colorResource(R.color.line))
            }
        },
        bottomBar = {
            NavigationBar {
                navController.navigate("home_screen")
            }
        },
        containerColor = Color.White,
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Content()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
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
            IconButton(onClick = {}, modifier = Modifier.padding(horizontal = 2.dp)) {
                Icon(
                    painterResource(R.drawable.backbutton),
                    contentDescription = "back",
                    tint = Color.Black
                )
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    painterResource(R.drawable.tick),
                    contentDescription = "add",
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
private fun Content() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(dimensionResource(R.dimen.padding_15))
    ) {
        item {
            ImageUploadSection()
            Spacer(modifier = Modifier.height(16.dp))
            WordInputSection()
            Spacer(modifier = Modifier.height(16.dp))
            TranslationSection()
            Spacer(modifier = Modifier.height(16.dp))
            ExampleUsageSection()
        }
    }
}

@Composable
private fun ImageUploadSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(colorResource(R.color.grey_graph), RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(R.drawable.image_placeholder),
                contentDescription = "Upload Image",
                tint = Color.Gray,
                modifier = Modifier.size(48.dp)
            )
            Text(text = "Картинка", color = Color.Gray, fontSize = 14.sp)
        }
    }
}

@Composable
private fun WordInputSection() {
    OutlinedTextField(
        value = "", // Здесь можно добавить состояние
        onValueChange = {},
        label = { Text("Слово") },
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.grey_graph)),
        trailingIcon = {
            Row {
                IconButton(onClick = {}) {
                    Icon(painterResource(R.drawable.volium), contentDescription = "Play Sound")
                }
                IconButton(onClick = {}) {
                    Icon(painterResource(R.drawable.close_icon), contentDescription = "Clear")
                }
            }
        },
    )
}

@Composable
private fun TranslationSection() {
    Column {
        OutlinedTextField(
            value = "", // Здесь можно добавить состояние
            onValueChange = {},
            label = { Text("Добавить свой перевод") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = {}) {
                    Icon(painterResource(R.drawable.close_icon), contentDescription = "Clear")
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(R.color.grey_graph), RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Text(
                text = "Слово [транскрипция] сущ\n1 перевод",
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
private fun ExampleUsageSection() {
    Column {
        OutlinedTextField(
            value = "", // Здесь можно добавить состояние
            onValueChange = {},
            label = { Text("Добавить свой пример") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = {}) {
                    Icon(painterResource(R.drawable.close_icon), contentDescription = "Clear")
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Примеры использования",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
    }
}

@Composable
@Preview(showSystemUi = true)
fun PreviewAddWord() {
    val navController = rememberNavController()
    AddWordScreen(navController)
}



package com.example.wordwave.presentation

import android.app.Application
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.wordwave.R
import com.example.wordwave.presentation.ViewModel
import kotlin.getValue

@Composable
fun AddWordScreen(navController: NavHostController, viewModel: ViewModel) {
    val (inputText, setInputText) = remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopBar(navController, viewModel)
        },
        bottomBar = {
            NavigationBar(navController)
        },
        modifier = Modifier.padding(WindowInsets.navigationBars.asPaddingValues()),
        containerColor = Color.White,
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(dimensionResource(R.dimen.padding_15))
                ) {
                    item {
                        ImageUploadSection()
                        Spacer(modifier = Modifier.height(16.dp))
                        WordInputSection(inputText = inputText, setInputText = setInputText)
                        Spacer(modifier = Modifier.height(16.dp))
                        TranslationSection()
                        Spacer(modifier = Modifier.height(16.dp))
                        ExampleUsageSection()
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navController: NavHostController, viewModel: ViewModel) {

    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.add_word_title),
                    fontSize = dimensionResource(R.dimen.title_size).value.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        navigationIcon = {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(horizontal = 2.dp)
            ) {
                Icon(
                    painterResource(R.drawable.backbutton),
                    contentDescription = "back",
                    tint = Color.White
                )
            }
        },
        actions = {
            IconButton(
                onClick =
                    {
                        viewModel.addSampleData()
                    })
            {
                Icon(
                    painterResource(R.drawable.tick),
                    contentDescription = "add",
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
private fun WordInputSection(inputText: String, setInputText: (String) -> Unit) {
    var localInputText by remember { mutableStateOf(inputText) }
    TextField(
        value = localInputText,
        onValueChange = {
            localInputText = it
            setInputText(it) // Обновляем родительский state
        },
        label = { Text("Слово") },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Gray,
            focusedContainerColor = colorResource(R.color.grey_graph),
            unfocusedContainerColor = colorResource(R.color.grey_graph),
            cursorColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        trailingIcon = {
            Row {
                IconButton(onClick = {}) {
                    Icon(
                        painterResource(R.drawable.volium),
                        tint = Color.Black,
                        contentDescription = "Play Sound"
                    )
                }
                IconButton(onClick = { localInputText = ""; setInputText("") }) {
                    Icon(
                        painterResource(R.drawable.close_icon),
                        tint = Color.Black,
                        contentDescription = "Clear"
                    )
                }
            }
        },
    )
}

@Composable
private fun TranslationSection() {
    Column {
        TextField(
            value = "",
            onValueChange = {},
            label = { Text("Добавить свой перевод") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Gray,
                focusedContainerColor = colorResource(R.color.grey_graph),
                unfocusedContainerColor = colorResource(R.color.grey_graph),
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            trailingIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        painterResource(R.drawable.close_icon),
                        tint = Color.Black,
                        contentDescription = "Clear"
                    )
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
        TextField(
            value = "",
            onValueChange = {},
            label = { Text("Добавить свой пример") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Gray,
                focusedContainerColor = colorResource(R.color.grey_graph),
                unfocusedContainerColor = colorResource(R.color.grey_graph),
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            trailingIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        painterResource(R.drawable.close_icon),
                        tint = Color.Black,
                        contentDescription = "Clear"
                    )
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
private fun PreviewAddWord() {
    val navController = rememberNavController()
    val viewModel: ViewModel = viewModel()
    AddWordScreen(navController, viewModel)
}

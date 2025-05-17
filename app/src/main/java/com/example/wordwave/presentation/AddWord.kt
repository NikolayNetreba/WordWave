package com.example.wordwave.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.wordwave.R
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun AddWordScreen(navController: NavHostController, viewModel: DictionaryViewModel, TviewModel: TranslationViewModel) {
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
                        WordInputSection(TviewModel)
                        Spacer(modifier = Modifier.height(16.dp))
                        ExampleUsageSection(TviewModel)
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navController: NavHostController, viewModel: DictionaryViewModel) {

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
                        val map = HashMap<String, List<String>>()
                        map.put("гора", listOf("porn", "porno", "anal", "masturbate"))
                        map.put("скала", listOf("porn", "porno", "anal", "masturbate"))

                        viewModel.addWordWithTranslations(
                            "mountain",
                             map
                        )
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

@OptIn(FlowPreview::class)
@Composable
private fun WordInputSection(viewModel: TranslationViewModel,) {
    val inputText by viewModel.inputText.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(inputText) {
        snapshotFlow { inputText }
            .debounce(250)
            .filter { it.isNotBlank() }
            .distinctUntilChanged()
            .collectLatest {
                viewModel.translateWord()
            }
    }

    val fontSize = with(LocalDensity.current) {
        when {
            inputText.length < 30 -> 24.sp
            inputText.length < 60 -> 22.sp
            else -> 20.sp
        }
    }

    TextField(
        value = inputText,
        onValueChange = viewModel::updateInputText,
        placeholder = { Text("Введите текст") },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedContainerColor = colorResource(R.color.grey_graph),
            unfocusedContainerColor = colorResource(R.color.grey_graph),
            cursorColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        textStyle = LocalTextStyle.current.copy(fontSize = fontSize),
        singleLine = true,
        trailingIcon = {
            if(inputText.isNotEmpty()) {
                Row {
                    IconButton(onClick = {}) {
                        Icon(
                            painterResource(R.drawable.volium),
                            tint = Color.Black,
                            contentDescription = "Play Sound"
                        )
                    }
                    IconButton(onClick = viewModel::clearInputText) {
                        Icon(
                            painter = painterResource(R.drawable.close_icon),
                            contentDescription = "Clear text",
                            tint = Color.Black,
                        )
                    }
                }
            }
        },
    )
    if(inputText.isNotEmpty()) {
        Spacer(modifier = Modifier.height(16.dp))

        val addTranslate = ""
        Column {
            TextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Добавить свой перевод") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = colorResource(R.color.grey_graph),
                    unfocusedContainerColor = colorResource(R.color.grey_graph),
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                trailingIcon = {
                    if (addTranslate.isNotEmpty()) {
                        IconButton(onClick = {}) {
                            Icon(
                                painterResource(R.drawable.close_icon),
                                tint = Color.Black,
                                contentDescription = "Clear"
                            )
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

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
}

@Composable
private fun ExampleUsageSection(viewModel: TranslationViewModel) {
    val inputText by viewModel.inputText.collectAsState()
    if(inputText.isNotEmpty()) {
        Column {
            TextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Добавить свой пример") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
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
}

/*@Composable
@Preview(showSystemUi = true)
private fun PreviewAddWord() {
    val navController = rememberNavController()
    val viewModel: FakeViewModel = viewModel()
    val TviewModel: TranslationViewModel = viewModel()
    AddWordScreen(navController, viewModel, TviewModel)
}*/

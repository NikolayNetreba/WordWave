package com.example.wordwave.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.wordwave.R
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.collectLatest

@Composable
fun TranslateScreen(
    navController: NavHostController,
    viewModel: TranslationViewModel
) {
    Scaffold(
        topBar = {
            Column {
                TopBar(navController, "Переводчик")
                HorizontalDivider(thickness = 1.dp, color = colorResource(R.color.line))
            }
        },
        bottomBar = { NavigationBar(navController) },
        containerColor = colorResource(R.color.greY),
        modifier = Modifier.padding(WindowInsets.navigationBars.asPaddingValues()),
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 10.dp)
                    .padding(top = 12.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                // Блок с выбором языка и полем ввода
                Box(
                    modifier = Modifier
                        .background(Color.White)
                ) {
                    Column {
                        LanguageSelector(viewModel)
                        HorizontalDivider(thickness = 1.dp, color = colorResource(R.color.line))
                        TranslateInputField(viewModel, navController)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                val inputText by viewModel.inputText.collectAsState()
                val definitions by viewModel.definitions.collectAsState()
                val examples by viewModel.examples.collectAsState()

                if(inputText.isNotEmpty()) {
                    // Блок с дополнительными переводами и примерами
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .padding(horizontal = 12.dp)
                            .padding(top = 12.dp)
                    ) {
                        definitions.forEach { def ->
                            item {
                                val title = buildString {
                                    append(def.text)
                                    def.pos?.let { append(" [$it]") }
                                }
                                SectionTitle(title)
                            }

                            items(def.tr.size) { index ->
                                val tr = def.tr[index]
                                Column(modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)) {
                                    val allSynonyms = buildList {
                                        add(tr.text)
                                        tr.syn?.forEach { add(it.text) }
                                    }

                                    Text(
                                        "${index + 1}. " + allSynonyms.joinToString(", "),
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 16.sp
                                    )

                                    tr.mean?.takeIf { it.isNotEmpty() }?.let { meanings ->
                                        Text(
                                            meanings.joinToString(", ") { it.text },
                                            color = Color.Gray,
                                            fontSize = 12.sp,
                                            softWrap = true
                                        )
                                    }
                                }
                            }
                        }

                        if (examples.isNotEmpty()) {
                            item {
                                SectionTitle("Примеры")
                            }

                            items(examples.size) { i ->
                                Text(
                                    "• ${examples[i]}",
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun LanguageSelector(viewModel: TranslationViewModel) {
    val fromLangCode = viewModel.fromLang.value
    val toLangCode = viewModel.toLang.value

    val languageMap = mapOf(
        "en" to "Английский",
        "ru" to "Русский",
        "tr" to "Турецкий",
        "de" to "Немецкий",
        "fr" to "Французский",
        "it" to "Итальянский",
        "es" to "Испанский"
    )

    var showSourceDropdown by remember { mutableStateOf(false) }
    var showTargetDropdown by remember { mutableStateOf(false) }

    val sourceName = languageMap[fromLangCode] ?: fromLangCode
    val targetName = languageMap[toLangCode] ?: toLangCode

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Box(modifier = Modifier
            .weight(1f)
            .wrapContentSize(Alignment.TopStart)
        ) {
            Button(
                onClick = { showSourceDropdown = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    sourceName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                )
            }

            DropdownMenu(
                expanded = showSourceDropdown,
                onDismissRequest = { showSourceDropdown = false }
            ) {
                languageMap.forEach { (code, name) ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            viewModel.setLanguagesSmart(code, isSource = true)
                            showSourceDropdown = false
                        }
                    )
                }
            }
        }

        IconButton(
            onClick = {
                viewModel.swapLanguages()
            },
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.swap_languages),
                contentDescription = "Swap Languages",
                tint = Color.Black
            )
        }
        
        Box(modifier = Modifier
            .weight(1f)
            .wrapContentSize(Alignment.TopStart)
        ) {
            Button(
                onClick = { showTargetDropdown = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    targetName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                )
            }

            DropdownMenu(
                expanded = showTargetDropdown,
                onDismissRequest = { showTargetDropdown = false }
            ) {
                languageMap.forEach { (code, name) ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            viewModel.setLanguagesSmart(code, isSource = false)
                            showTargetDropdown = false
                        }
                    )
                }
            }
        }
    }
}



@OptIn(FlowPreview::class)
@Composable
fun TranslateInputField(
    viewModel: TranslationViewModel,
    navController: NavHostController
) {
    val inputText by viewModel.inputText.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val primaryTranslation by viewModel.primaryTranslation.collectAsState()

    val fontSize = with(LocalDensity.current) {
        when {
            inputText.length < 30 -> 24.sp
            inputText.length < 60 -> 22.sp
            else -> 20.sp
        }
    }

    LaunchedEffect(inputText) {
        snapshotFlow { inputText }
            .debounce(250)
            .filter { it.isNotBlank() }
            .distinctUntilChanged()
            .collectLatest {
                viewModel.translateWord()
            }
    }

    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .clip(RoundedCornerShape(12.dp))
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 50.dp, max = 150.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextField(
                    value = inputText,
                    onValueChange = viewModel::updateInputText,
                    placeholder = { Text("Введите текст") },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        cursorColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = LocalTextStyle.current.copy(fontSize = fontSize),
//                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
//                    keyboardActions = KeyboardActions(
//                        onSearch = {
//                            viewModel.translateWord()
//                            keyboardController?.hide()
//                        }
//                    ),
                    singleLine = true,
                    trailingIcon = {
                        if (inputText.isNotEmpty()) {
                            IconButton(onClick = viewModel::clearInputText) {
                                Icon(
                                    painter = painterResource(id = R.drawable.add_2_24),
                                    contentDescription = "Clear text",
                                    tint = Color.Black,
                                    modifier = Modifier.rotate(45f)
                                )
                            }
                        }
                    }
                )
            }

            if (inputText.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { /* Прослушивание */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.volium),
                            contentDescription = "Play text",
                            tint = Color.Black
                        )
                    }
                    IconButton(onClick = { navController.navigate("add_word_screen") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.add_2_24),
                            contentDescription = "Add word",
                            tint = Color.Black
                        )
                    }
                }

                if (primaryTranslation.isNotEmpty()) {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = colorResource(id = R.color.line)

                    )
                    errorMessage?.let {
                        Text(
                            text = it,
                            color = Color.Red,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }
                    // Основной перевод

                    Text(
                        primaryTranslation,
                        fontSize = fontSize,
                        modifier = Modifier
                            .padding(8.dp)
                    )

                    // Кнопка прослушивания перевода
                    IconButton(onClick = { /* Прослушивание перевода */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.volium),
                            contentDescription = "Play translation",
                            tint = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        color = Color.Black,
        modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
    )
}

@Composable
@Preview(showSystemUi = true)
private fun PreviewTranslate() {
    val navController = rememberNavController()
    val viewModel: TranslationViewModel = viewModel()
    TranslateScreen(navController, viewModel)
}

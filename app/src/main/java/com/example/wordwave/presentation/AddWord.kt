package com.example.wordwave.presentation

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.wordwave.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.FlowPreview

@Composable
fun AddWordScreen(
    navController: NavHostController,
    dViewModel: DictionaryViewModel,
    TviewModel: TranslationViewModel
) {
    val (inputText, setInputText) = remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopBar(navController, dViewModel, TviewModel)
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
                        ImageUploadSection(dViewModel)
                        Spacer(modifier = Modifier.height(16.dp))
                        WordInputSection(TviewModel, dViewModel)
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
private fun TopBar(
    navController: NavHostController,
    DviewModel: DictionaryViewModel,
    TviewModel: TranslationViewModel
) {

    val context = LocalContext.current
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
                        DviewModel.saveDefinitions()
                        navController.popBackStack()
                        Toast.makeText(context, "Слово сохранено", Toast.LENGTH_SHORT).show()
                        TviewModel.clearInputText()
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
private fun ImageUploadSection(dViewModel: DictionaryViewModel) {
    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var menuExpanded by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imageUri = it
            dViewModel.currentImagePath = it.toString()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(colorResource(R.color.grey_graph))
            .clickable { menuExpanded = true },
        contentAlignment = Alignment.Center
    ) {
        // Меню выбора
        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Выбрать из галереи") },
                onClick = {
                    menuExpanded = false
                    launcher.launch("image/*")
                }
            )
        }

        // Показ выбранной картинки или заглушки
        val displayUri = imageUri ?: dViewModel.currentImagePath?.let(Uri::parse)
        if (displayUri != null) {
            AsyncImage(
                model = displayUri,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painterResource(R.drawable.image_placeholder),
                    contentDescription = "Upload Image",
                    tint = Color.Gray,
                    modifier = Modifier.size(48.dp)
                )
                Text("Картинка", color = Color.Gray, fontSize = 14.sp)
            }
        }
    }
}

@OptIn(FlowPreview::class)
@Composable
private fun WordInputSection(tViewModel: TranslationViewModel, dViewModel: DictionaryViewModel) {
    val inputText by tViewModel.inputText.collectAsState()
    val definitions by tViewModel.definitions.collectAsState()
    val errorMessage by tViewModel.errorMessage.collectAsState()
    dViewModel.currentDefinitions = definitions
    var addTranslate by rememberSaveable { mutableStateOf("") }


    LaunchedEffect(inputText) {
        snapshotFlow { inputText }
            .debounce(250)
            .filter { it.isNotBlank() }
            .distinctUntilChanged()
            .collectLatest {
                tViewModel.translateWord()
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
        onValueChange = tViewModel::updateInputText,
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
            if (inputText.isNotEmpty()) {
                Row {
                    IconButton(onClick = {}) {
                        Icon(
                            painterResource(R.drawable.volium),
                            tint = Color.Black,
                            contentDescription = "Play Sound"
                        )
                    }
                    IconButton(onClick = tViewModel::clearInputText) {
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
    if (inputText.isNotEmpty()) {
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(colorResource(R.color.grey_graph))
                .padding(horizontal = 12.dp)
        ) {
            dViewModel.currentDefinitions = definitions
            dViewModel.currentWord = inputText
            Log.println(Log.ASSERT, null, inputText + " " + (definitions.isNotEmpty()))
            definitions.forEach { def ->
                val title = buildString {
                    append(def.text)
                    def.pos?.let { append(" [$it]") }
                }
                SectionTitle(title)

                def.tr.forEachIndexed { index, tr ->
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
        }
        Spacer(modifier = Modifier.height(16.dp))

        Column {
            TextField(
                value = addTranslate,
                onValueChange = { addTranslate = it },
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

                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (addTranslate.isNotBlank()) {
                            addTranslate = ""
                        }
                    }
                ),
                trailingIcon = {
                    if (addTranslate.isNotEmpty()) {
                        IconButton(onClick = { addTranslate = "" }) {
                            Icon(
                                painterResource(R.drawable.close_icon),
                                contentDescription = "Clear"
                            )
                        }
                    }
                }
            )
        }
    }
}


@Composable
private fun ExampleUsageSection(viewModel: TranslationViewModel) {
    val inputText by viewModel.inputText.collectAsState()
    val examples by viewModel.examples.collectAsState()
    if (inputText.isNotEmpty()) {
        var addExample by rememberSaveable { mutableStateOf("") }


        // Поле для добавления примера
        TextField(
            value = addExample,
            onValueChange = { addExample = it },
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
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (addExample.isNotBlank()) {
                        // Добавь логику сохранения примера в ViewModel
                        addExample = ""
                    }
                }
            ),
            trailingIcon = {
                if (addExample.isNotEmpty()) {
                    IconButton(onClick = { addExample = "" }) {
                        Icon(
                            painterResource(R.drawable.close_icon),
                            contentDescription = "Clear"
                        )
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (examples.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(colorResource(R.color.grey_graph))
                    .padding(horizontal = 12.dp)
            ) {
                SectionTitle("Примеры")
                examples.forEach { example ->
                    Text(
                        text = "• $example",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
                    )
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
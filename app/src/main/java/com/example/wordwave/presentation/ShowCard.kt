package com.example.wordwave.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import android.net.Uri
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.wordwave.R
import androidx.core.net.toUri

@Composable
fun ShowCardScreen(navController: NavHostController, viewModel: DictionaryViewModel) {
    Scaffold(
        topBar = { TopBar(navController, viewModel) },
        modifier = Modifier.padding(WindowInsets.navigationBars.asPaddingValues()),
        containerColor = Color.White,
        bottomBar = { NavigationBar(navController) },
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
                        ImageUploadSection(viewModel)
                        Spacer(modifier = Modifier.height(16.dp))
                        ShowTranslate(viewModel)
                    }
                }
            }
        },
    )
}

@Composable
private fun ImageUploadSection(viewModel: DictionaryViewModel) {
    val imagePath = viewModel.cr?.word?.imagePath
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(colorResource(R.color.grey_graph)),
        contentAlignment = Alignment.Center
    ) {
        if (imagePath != null) {
            AsyncImage(
                model = imagePath.toUri(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(R.drawable.image_placeholder),
                    contentDescription = "No image",
                    tint = Color.Gray,
                    modifier = Modifier.size(48.dp)
                )
                Text(text = "Картинка", color = Color.Gray, fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun ShowTranslate(viewModel: DictionaryViewModel) {
    var wwt = viewModel.cr!!

    wwt.translations.forEachIndexed { index, tr ->
        Column(modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)) {
            val allSynonyms = buildList {
                add(tr.translation.translatedText)
                tr.synonyms.forEach { add(it.text) }
            }

            Text(
                "${index + 1}. " + allSynonyms.joinToString(", "),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        }
    }
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
                    text = viewModel.cr?.word?.text ?: "",
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

                    })
            {
                Icon(
                    painterResource(R.drawable.pencil_alt),
                    contentDescription = "refactor",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(R.color.bar)
        )
    )
}
package com.example.wordwave.presentation.game

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.wordwave.presentation.FlashCardsViewModel

@Composable
fun FlashCardsScreen(navController: NavHostController, viewModel: FlashCardsViewModel) {
    val currentWord = viewModel.currentWord
    val isFlipped by viewModel.isFlipped
    val index by viewModel.cardIndex

    val transition = updateTransition(targetState = isFlipped, label = "cardFlip")
    val rotationY by transition.animateFloat(label = "rotationY") { flipped ->
        if (flipped) 180f else 0f
    }

    val density = LocalDensity.current.density
    val showBack = rotationY > 90f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.6f)
                .graphicsLayer {
                    this.rotationY = rotationY
                    this.cameraDistance = 12f * density
                }
                .clickable {
                    if (!isFlipped) viewModel.flipCard()
                }
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                tonalElevation = 6.dp,
                color = MaterialTheme.colorScheme.surfaceVariant,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (!showBack) {
                        Text(
                            text = currentWord.word,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer { scaleX = -1f },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Перевод теперь того же размера и по центру
                            Text(
                                text = currentWord.translation,
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                            )

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                            ) {
                                Spacer(modifier = Modifier.weight(1f))
                                Button(onClick = {
                                    viewModel.onRememberClicked()
                                    viewModel.flipCard()
                                    viewModel.nextCard()
                                }) {
                                    Text("Помню")
                                }
                                Button(onClick = {
                                    viewModel.onDontRememberClicked()
                                    viewModel.flipCard()
                                    viewModel.nextCard()
                                }) {
                                    Text("Не помню")
                                }
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
                .align(Alignment.BottomCenter)
        ) {
            Text(
                text = "${index + 1}/10",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }
    }
}
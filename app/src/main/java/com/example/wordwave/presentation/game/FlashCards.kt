package com.example.wordwave.presentation.game

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
                            text = currentWord?.first ?: "",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    } else {
                        Column(Modifier.fillMaxSize().scale(-1f, 1f)) {
                            // 1) Заголовок
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp, vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = currentWord?.first ?: "",
                                    style = MaterialTheme.typography.headlineMedium,
                                    textAlign = TextAlign.Center
                                )
                            }

                            // 2) Первый Divider с padding
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )

                            // 3) Список переводов
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp, vertical = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                viewModel.getAllTranslationsForCurrentWord().forEach { tr ->
                                    Text(
                                        text = tr,
                                        style = MaterialTheme.typography.headlineSmall,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            // 4) Второй Divider с таким же padding
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )

                            // 5) Кнопки прижаты к низу
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp)
                                    .padding(top = 12.dp, bottom = 24.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                TextButton(
                                    onClick = { viewModel.onRememberClicked() },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF4CAF50))
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(20.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text("Помню", style = MaterialTheme.typography.titleMedium)
                                }
                                Spacer(Modifier.width(16.dp))
                                TextButton(
                                    onClick = { viewModel.onDontRememberClicked() },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurfaceVariant)
                                ) {
                                    Text("Не помню", style = MaterialTheme.typography.titleMedium)
                                }
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
                text = "${index + 1}/${viewModel.wordsNum}",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }
    }
}

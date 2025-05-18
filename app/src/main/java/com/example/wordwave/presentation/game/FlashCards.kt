package com.example.wordwave.presentation.game

import android.util.Log
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
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
import com.example.wordwave.presentation.WORDS_NUM

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

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                // только вертикальный паддинг
                                .padding(vertical = 0.dp)
                        ) {
                            // === Первая линия сразу под верхом карточки ===
                            Divider(
                                modifier = Modifier.fillMaxWidth(),
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )

                            // === Блок контента со своими отступами ===
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp, vertical = 16.dp),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // заголовок (слово)
                                Text(
                                    text = currentWord?.first ?: "",
                                    style = MaterialTheme.typography.headlineMedium,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(Modifier.height(12.dp))

                                // список переводов, чуть меньше заголовка
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
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
                            }

                            // === Вторая линия над кнопками ===
                            Divider(
                                modifier = Modifier.fillMaxWidth(),
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )

                            // === Кнопки, прижаты к низу с небольшим паддингом снизу ===
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                TextButton(
                                    onClick = { viewModel.onRememberClicked() },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF4CAF50))
                                ) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
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

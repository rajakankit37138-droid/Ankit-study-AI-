package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CyberBadge
import com.example.ui.components.GlassCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenDestination

@Composable
fun FlashcardScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler { viewModel.navigateTo(ScreenDestination.Home) }

    val flashcards by viewModel.repository.flashcards.collectAsState()
    val activeIndex by viewModel.activeFlashcardIndex.collectAsState()
    var isFlipped by remember { mutableStateOf(false) }

    val currentCard = flashcards.getOrNull(activeIndex) ?: flashcards.first()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceDark)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Top Navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.navigateTo(ScreenDestination.Home) },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(SpaceCard)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = NeonCyan
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "AI FLASHCARDS (SPACED REPETITION)",
                    color = LightWhite,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Swipe / Tap to Flip • Leitner Retention Intervals",
                    color = SoftText,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Progress Pill
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CyberBadge(text = "${currentCard.subject.uppercase()} • ${currentCard.chapter}", color = NeonCyan)
            Text(
                text = "Card ${activeIndex + 1} of ${flashcards.size}",
                color = SoftText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 3D Flip Flashcard Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.verticalGradient(
                        if (isFlipped) listOf(SpaceCardElevated, SpaceCard) else listOf(SpaceCard, SpaceCardElevated)
                    )
                )
                .border(1.5.dp, if (isFlipped) CyberGreen.copy(alpha = 0.6f) else NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                .clickable { isFlipped = !isFlipped }
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (isFlipped) "BACK (SOLUTION / EXPLANATION)" else "FRONT (CONCEPT PROMPT)",
                        color = if (isFlipped) CyberGreen else NeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "Tap to Flip 🔄", color = SoftText, fontSize = 11.sp)
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = if (isFlipped) currentCard.backExplanation else currentCard.frontPrompt,
                        color = LightWhite,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    )

                    currentCard.keyFormula?.let { formula ->
                        if (isFlipped) {
                            Spacer(modifier = Modifier.height(14.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SpaceDark)
                                    .border(1.dp, HologramGold.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = formula,
                                    color = HologramGold,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Text(
                    text = "Next review interval: ${currentCard.intervalDays} day(s)",
                    color = SoftText,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Actions: Don't Know (Left) vs Know (Right)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Don't Know Button
            Button(
                onClick = {
                    isFlipped = false
                    viewModel.nextFlashcard(known = false)
                },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .padding(end = 8.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SpaceCard),
                border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(AlertRed, FlameAmber)))
            ) {
                Icon(Icons.Default.Close, contentDescription = null, tint = AlertRed)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Don't Know", color = AlertRed, fontWeight = FontWeight.Bold)
            }

            // Know Button
            Button(
                onClick = {
                    isFlipped = false
                    viewModel.nextFlashcard(known = true)
                },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .padding(start = 8.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CyberGreen)
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = SpaceDark)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Know (+15 XP)", color = SpaceDark, fontWeight = FontWeight.Bold)
            }
        }
    }
}

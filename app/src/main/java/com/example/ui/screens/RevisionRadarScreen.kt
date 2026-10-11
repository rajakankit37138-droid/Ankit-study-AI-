package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.RevisionRadarItem
import com.example.data.models.UrgencyLevel
import com.example.ui.components.CyberBadge
import com.example.ui.components.GlassCard
import com.example.ui.components.GradientButton
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RevisionRadarScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler { viewModel.navigateTo(ScreenDestination.Home) }

    val revisionItems by viewModel.repository.revisionRadarItems.collectAsState()
    var selectedItemId by remember { mutableStateOf<String?>(revisionItems.firstOrNull()?.id) }
    var activeMiniQuizItem by remember { mutableStateOf<RevisionRadarItem?>(null) }
    var selectedQuizOption by remember { mutableStateOf<Int?>(null) }
    var quizSubmitted by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceDark)
            .padding(horizontal = 16.dp)
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
                    text = "SMART REVISION RADAR",
                    color = LightWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Signature AI: Decaying Retention & Ebbinghaus Curve",
                    color = SoftText,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (activeMiniQuizItem != null) {
            // Mini Quiz View
            val quiz = activeMiniQuizItem!!.practiceQuestions.firstOrNull()
            if (quiz != null) {
                GlassCard(borderColor = NeonCyan.copy(alpha = 0.5f)) {
                    CyberBadge(text = "MINI-TEST • ${activeMiniQuizItem!!.subject}", color = NeonCyan)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = quiz.questionText, color = LightWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(12.dp))
                    quiz.options.forEachIndexed { idx, opt ->
                        val isSelected = selectedQuizOption == idx
                        val bg = when {
                            quizSubmitted && idx == quiz.correctIndex -> CyberGreen.copy(alpha = 0.25f)
                            quizSubmitted && isSelected && idx != quiz.correctIndex -> AlertRed.copy(alpha = 0.25f)
                            isSelected -> ElectricBlue.copy(alpha = 0.3f)
                            else -> SpaceDark
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(bg)
                                .border(1.dp, SpaceCardBorder, RoundedCornerShape(8.dp))
                                .clickable(enabled = !quizSubmitted) { selectedQuizOption = idx }
                                .padding(10.dp)
                        ) {
                            Text(text = opt, color = LightWhite, fontSize = 13.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (!quizSubmitted && selectedQuizOption != null) {
                        Button(
                            onClick = {
                                quizSubmitted = true
                                viewModel.repository.awardXp(30)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                        ) {
                            Text("Submit Mini-Test", color = SpaceDark, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (quizSubmitted) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (selectedQuizOption == quiz.correctIndex) "🎉 Great job! Retention refreshed to 98%!" else "Review notes below to fix this slip.",
                            color = if (selectedQuizOption == quiz.correctIndex) CyberGreen else AlertRed,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                activeMiniQuizItem = null
                                selectedQuizOption = null
                                quizSubmitted = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SpaceCardBorder)
                        ) {
                            Text("Back to Revision Radar", color = LightWhite)
                        }
                    }
                }
            }
        } else {
            // List of Today's Revision Targets
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 96.dp)
            ) {
                items(revisionItems) { item ->
                    val isSelected = selectedItemId == item.id
                    val urgencyColor = when (item.urgency) {
                        UrgencyLevel.HIGH -> AlertRed
                        UrgencyLevel.MEDIUM -> FlameAmber
                        UrgencyLevel.LOW -> HologramGold
                    }

                    GlassCard(
                        borderColor = urgencyColor.copy(alpha = 0.6f),
                        backgroundColor = if (isSelected) SpaceCardElevated else SpaceCard,
                        modifier = Modifier.clickable { selectedItemId = item.id }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(urgencyColor)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${item.subject} — ${item.chapter}",
                                    color = LightWhite,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            CyberBadge(text = item.urgency.label, color = urgencyColor)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Stats & Reason
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Retention: ${item.retentionScorePercent}%",
                                color = urgencyColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "⏱️ Rec: ${item.recommendedStudyMinutes} mins",
                                color = SoftText,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Why revise now: ${item.reason}",
                            color = SoftText,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(SpaceDark)
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "Quick Notes: ${item.quickNotesSummary}",
                                color = LightWhite,
                                fontSize = 12.sp,
                                lineHeight = 17.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(onClick = {
                                viewModel.sendChatQuery("Teach me ${item.chapter} revision tips", mode = "simple")
                                viewModel.navigateTo(ScreenDestination.AnkitAi)
                            }) {
                                Text("Ask ANKIT AI", color = NeonCyan, fontSize = 12.sp)
                            }

                            GradientButton(
                                text = "Start Mini-Test (5 Qs)",
                                onClick = {
                                    activeMiniQuizItem = item
                                    selectedQuizOption = null
                                    quizSubmitted = false
                                },
                                gradient = Brush.horizontalGradient(listOf(urgencyColor, FlameAmber)),
                                contentColor = Color.White,
                                height = 36.dp
                            )
                        }
                    }
                }
            }
        }
    }
}

package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.CyberBadge
import com.example.ui.components.GlassCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenDestination

@Composable
fun VoiceTeacherScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler { viewModel.navigateTo(ScreenDestination.Home) }

    var isListening by remember { mutableStateOf(false) }
    var spokenVoiceQuery by remember { mutableStateOf("Mujhe Newton's laws simple language mein samjhao.") }
    var voiceResponse by remember {
        mutableStateOf(
            "Haan bilkul Aman! Newton ke 3 laws simple Hinglish me samjho:\n\n1. First Law (Inertia): Jab tak koi bahari force na lage, body jaise hai waise hi rahegi. Agar resting hai toh rest karegi, moving hai toh move karti rahegi!\n\n2. Second Law (F = ma): Force barabar mass guna acceleration. Badi gaadi ko tezi se bhagane ke liye bada engine chahiye!\n\n3. Third Law (Action-Reaction): Har action ka equal and opposite reaction hota hai. Swimmer pani ko piche dhakelta hai, aur pani usko aage push karta hai!\n\nKya iska koi numerical test karein?"
        )
    }
    var isSpeakingAudio by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val waveScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave_scale"
    )

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
                    text = "VOICE AI TEACHER",
                    color = LightWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Natural Voice Tutoring in Hindi & Hinglish",
                    color = SoftText,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 4K Voice Teacher Avatar & Audio Aura
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size((140 * if (isListening || isSpeakingAudio) waveScale else 1f).dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        if (isListening) AlertRed.copy(alpha = 0.5f) else NeonCyan.copy(alpha = 0.35f),
                                        if (isListening) FlameAmber.copy(alpha = 0.25f) else CyberViolet.copy(alpha = 0.2f),
                                        Color.Transparent
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .clip(CircleShape)
                                .border(
                                    2.5.dp,
                                    if (isListening) AlertRed else NeonCyan,
                                    CircleShape
                                )
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_ai_tutor_avatar),
                                contentDescription = "ANKIT AI Voice Mentor",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CyberBadge(
                            text = if (isListening) "LISTENING (MIC ACTIVE)" else if (isSpeakingAudio) "SPEAKING (4K AUDIO)" else "AI MENTOR READY",
                            color = if (isListening) AlertRed else if (isSpeakingAudio) HologramGold else NeonCyan
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = when {
                            isListening -> "Listening to your voice... (बोलिए)"
                            isSpeakingAudio -> "ANKIT AI is speaking... (सुनिए)"
                            else -> "Tap microphone or choose sample voice query"
                        },
                        color = if (isListening) AlertRed else NeonCyan,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Student Spoken Query Card
            item {
                GlassCard(borderColor = CyberViolet.copy(alpha = 0.5f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CyberBadge(text = "YOU SAID (VOICE)", color = CyberViolet)
                        Text(text = "Hinglish Natural", color = SoftText, fontSize = 11.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "«“$spokenVoiceQuery”»",
                        color = LightWhite,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // AI Teacher Spoken Response Card
            item {
                GlassCard(
                    borderColor = NeonCyan.copy(alpha = 0.5f),
                    backgroundColor = SpaceCardElevated
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CyberBadge(text = "ANKIT AI VOICE RESPONSE", color = NeonCyan)
                        Text(text = "Teacher Ankit Rajak", color = SoftText, fontSize = 11.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = voiceResponse,
                        color = LightWhite,
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Buttons requested in prompt: Listen | Repeat | Explain Again | Ask Follow-up
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        VoiceActionPill(
                            label = if (isSpeakingAudio) "Stop" else "Listen 🔊",
                            color = NeonCyan,
                            onClick = { isSpeakingAudio = !isSpeakingAudio }
                        )
                        VoiceActionPill(
                            label = "Repeat 🔁",
                            color = HologramGold,
                            onClick = { isSpeakingAudio = true }
                        )
                        VoiceActionPill(
                            label = "Explain Again 🔄",
                            color = CyberViolet,
                            onClick = {
                                viewModel.sendChatQuery("Explain again in simpler words: $spokenVoiceQuery", mode = "simple")
                                viewModel.navigateTo(ScreenDestination.AnkitAi)
                            }
                        )
                        VoiceActionPill(
                            label = "Follow-up ❓",
                            color = CyberGreen,
                            onClick = {
                                viewModel.sendChatQuery("Follow-up question on: $spokenVoiceQuery")
                                viewModel.navigateTo(ScreenDestination.AnkitAi)
                            }
                        )
                    }
                }
            }
        }

        // Bottom Voice Control
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = { isListening = !isListening },
                modifier = Modifier
                    .height(50.dp)
                    .clip(RoundedCornerShape(25.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = if (isListening) AlertRed else NeonCyan)
            ) {
                Icon(
                    imageVector = if (isListening) Icons.Default.Stop else Icons.Default.Mic,
                    contentDescription = null,
                    tint = SpaceDark
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isListening) "Stop Listening" else "Hold to Speak to ANKIT AI",
                    color = SpaceDark,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun VoiceActionPill(
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.15f))
            .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Text(text = label, color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

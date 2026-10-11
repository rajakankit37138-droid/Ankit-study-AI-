package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CyberBadge
import com.example.ui.components.GlassCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenDestination

@Composable
fun FocusModeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler { viewModel.navigateTo(ScreenDestination.Home) }

    val secondsRemaining by viewModel.focusSecondsRemaining.collectAsState()
    val isRunning by viewModel.isFocusRunning.collectAsState()
    val userProfile by viewModel.repository.userProfile.collectAsState()

    var selectedAmbientSound by remember { mutableStateOf("Binaural Alpha Waves") }
    val ambientSounds = listOf("Binaural Alpha Waves", "Rainy Library", "Cyber Deep Space", "White Noise")

    val minutes = secondsRemaining / 60
    val seconds = secondsRemaining % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)
    val progress = 1f - (secondsRemaining / (25f * 60f))

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
                    text = "DEEP FOCUS & POMODORO",
                    color = LightWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Distraction-Free AI Study Chamber",
                    color = SoftText,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Exam Countdown Banner
        GlassCard(borderColor = HologramGold.copy(alpha = 0.4f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "EXAM COUNTDOWN", color = HologramGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(text = userProfile.targetExam, color = LightWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
                CyberBadge(text = "218 DAYS LEFT", color = HologramGold)
            }
        }

        Spacer(modifier = Modifier.height(26.dp))

        // Big Futuristic Circular Timer
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(240.dp)
        ) {
            CircularProgressIndicator(
                progress = { 1f },
                modifier = Modifier.size(230.dp),
                color = SpaceCardBorder,
                strokeWidth = 10.dp,
                trackColor = Color.Transparent
            )
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.size(230.dp),
                color = NeonCyan,
                strokeWidth = 10.dp,
                trackColor = Color.Transparent
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = formattedTime,
                    color = LightWhite,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                )
                Text(
                    text = if (isRunning) "FOCUSING DEEPLY" else "READY TO STUDY",
                    color = if (isRunning) NeonCyan else SoftText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "+100 XP upon completion", color = CyberGreen, fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(26.dp))

        // Timer Controls
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.resetFocusTimer() },
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(SpaceCard)
                    .border(1.dp, SpaceCardBorder, CircleShape)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = SoftText)
            }

            Button(
                onClick = { viewModel.toggleFocusTimer() },
                modifier = Modifier
                    .height(56.dp)
                    .width(160.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (isRunning) AlertRed else NeonCyan)
            ) {
                Icon(
                    imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = SpaceDark
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isRunning) "Pause" else "Start Focus",
                    color = SpaceDark,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Ambient Sound Generator
        GlassCard {
            Text(text = "🎵 Neural Ambient Sound:", color = LightWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(ambientSounds) { sound ->
                    val isSelected = selectedAmbientSound == sound
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) CyberViolet else SpaceDark)
                            .border(1.dp, if (isSelected) CyberViolet else SpaceCardBorder, RoundedCornerShape(8.dp))
                            .clickable { selectedAmbientSound = sound }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = sound,
                            color = if (isSelected) Color.White else SoftText,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

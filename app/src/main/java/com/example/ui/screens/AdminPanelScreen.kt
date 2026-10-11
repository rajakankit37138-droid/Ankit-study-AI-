package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
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
import com.example.ui.components.CyberBadge
import com.example.ui.components.GlassCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenDestination

@Composable
fun AdminPanelScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler { viewModel.navigateTo(ScreenDestination.Profile) }

    var broadcastText by remember { mutableStateOf("") }
    var broadcastSent by remember { mutableStateOf(false) }

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
                onClick = { viewModel.navigateTo(ScreenDestination.Profile) },
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
                    text = "ANKIT RAJAK • ADMIN DASHBOARD",
                    color = LightWhite,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Platform Management • Content Engine • Analytics",
                    color = SoftText,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            // Live Stats Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AdminStatCard(
                        title = "TOTAL STUDENTS",
                        value = "24,850",
                        subtitle = "+1,240 this week",
                        accentColor = NeonCyan,
                        modifier = Modifier.weight(1f)
                    )
                    AdminStatCard(
                        title = "ACTIVE DOUBTS",
                        value = "8,420",
                        subtitle = "99.4% AI resolved",
                        accentColor = CyberGreen,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AdminStatCard(
                        title = "QUESTION BANK",
                        value = "142,500",
                        subtitle = "12 Target exams",
                        accentColor = HologramGold,
                        modifier = Modifier.weight(1f)
                    )
                    AdminStatCard(
                        title = "AI ENGINE HEALTH",
                        value = "OPTIMAL",
                        subtitle = "Gemini Flash / Low Latency",
                        accentColor = CyberViolet,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Content Management Categories
            item {
                GlassCard {
                    Text(
                        text = "Content & Curriculum Management",
                        color = LightWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    val adminModules = listOf(
                        "Manage Classes & Exam Syllabi (Class 6-12, JEE, NEET, UPSC, SSC)",
                        "Question Banks & Mock Test Upload",
                        "Notes, PDFs & Formula Sheet Repository",
                        "Daily Current Affairs & Editorial Quiz Feed",
                        "AI Learning Memory Calibration & Radar Weights"
                    )

                    adminModules.forEach { module ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(SpaceDark)
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "⚙️ $module", color = LightWhite, fontSize = 12.sp, modifier = Modifier.weight(1f))
                            Text(text = "Manage →", color = NeonCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Student Push Notification Broadcast
            item {
                GlassCard(borderColor = CyberViolet.copy(alpha = 0.5f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CyberBadge(text = "PUSH BROADCAST", color = CyberViolet)
                        Text(text = "Reach 24.8k Students", color = SoftText, fontSize = 11.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Send Instant Exam Alert or Motivation to All Students:",
                        color = LightWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = broadcastText,
                        onValueChange = { broadcastText = it },
                        placeholder = { Text("Enter notification text (e.g. JEE Main Mock Test #4 is live!)", color = SoftText, fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberViolet,
                            unfocusedBorderColor = SpaceCardBorder,
                            focusedTextColor = LightWhite,
                            unfocusedTextColor = LightWhite
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            if (broadcastText.isNotBlank()) {
                                broadcastSent = true
                                broadcastText = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberViolet)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Broadcast Message", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    if (broadcastSent) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "✅ Broadcast sent successfully to all active student devices!", color = CyberGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun AdminStatCard(
    title: String,
    value: String,
    subtitle: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(SpaceCard)
            .border(1.dp, accentColor.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Column {
            Text(text = title, color = SoftText, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, color = accentColor, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, color = SoftText, fontSize = 10.sp)
        }
    }
}

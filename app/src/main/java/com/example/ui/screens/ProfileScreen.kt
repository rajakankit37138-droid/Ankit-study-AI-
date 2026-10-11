package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.ui.components.GradientButton
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.repository.userProfile.collectAsState()
    val learningMemory by viewModel.repository.learningMemory.collectAsState()
    var showExamDialog by remember { mutableStateOf(false) }

    val allExams = listOf(
        "JEE Main & Advanced",
        "NEET (Medical)",
        "UPSC Civil Services",
        "SSC CGL / CHSL",
        "Banking (IBPS / SBI PO)",
        "Railway (RRB NTPC / Group D)",
        "State PSC",
        "Defence (NDA / CDS / AFCAT)",
        "Police SI / Constable",
        "Teaching (CTET / State TET)",
        "Class 11–12 Board",
        "Class 6–10 Foundation"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceDark)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Profile Header
        item {
            GlassCard(
                borderColor = NeonCyan.copy(alpha = 0.4f),
                backgroundColor = SpaceCardElevated
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(NeonCyan, CyberViolet)))
                            .border(2.dp, LightWhite, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "AK", color = SpaceDark, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = userProfile.name,
                                color = LightWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            CyberBadge(text = "LVL ${userProfile.level}", color = NeonCyan)
                        }
                        Text(
                            text = "${userProfile.levelTitle} • ${userProfile.studentClass}",
                            color = SoftText,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Target: ${userProfile.targetExam}",
                            color = HologramGold,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    IconButton(onClick = { showExamDialog = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Exam", tint = NeonCyan)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = SpaceCardBorder)
                Spacer(modifier = Modifier.height(12.dp))

                // Stats Grid (SVG Vector Icons)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    ProfileStatItem(Icons.Default.LocalFireDepartment, "Streak", "${userProfile.studyStreakDays} Days", FlameAmber)
                    ProfileStatItem(Icons.Default.Bolt, "XP", "${userProfile.totalXp}", NeonCyan)
                    ProfileStatItem(Icons.Default.MonetizationOn, "Coins", "${userProfile.studyCoins}", HologramGold)
                    ProfileStatItem(Icons.Default.Timer, "Goal", "${userProfile.dailyStudyGoalMinutes / 60}h / day", CyberGreen)
                }
            }
        }

        // AI Learning Memory Engine
        item {
            GlassCard(
                borderColor = CyberViolet.copy(alpha = 0.5f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(CyberViolet.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = "AI Memory",
                                tint = CyberViolet,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "AI LEARNING MEMORY ENGINE",
                                color = LightWhite,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Continuous behavioral pattern analysis",
                                color = SoftText,
                                fontSize = 11.sp
                            )
                        }
                    }
                    CyberBadge(text = "ACTIVE", color = CyberGreen)
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Frequently Wrong
                Text(text = "Frequently Wrong Topics (High Priority):", color = AlertRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                learningMemory.frequentlyWrongTopics.forEach { topic ->
                    Text(text = "• $topic", color = LightWhite, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Strong vs Weak
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Strong Subjects:", color = CyberGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        learningMemory.strongSubjects.take(2).forEach {
                            Text(text = "✓ $it", color = SoftText, fontSize = 11.sp)
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Weak Chapters:", color = FlameAmber, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        learningMemory.weakChapters.take(2).forEach {
                            Text(text = "⚠ $it", color = SoftText, fontSize = 11.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Explanation Style
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Preferred Style:", color = SoftText, fontSize = 12.sp)
                    Text(text = learningMemory.preferredExplanationStyle, color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Achievements & Badges
        item {
            GlassCard {
                Text(
                    text = "Achievements & Badges",
                    color = LightWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(userProfile.badges) { badge ->
                        CyberBadge(text = badge, color = HologramGold)
                    }
                }
            }
        }

        // Admin Panel Shortcut for Ankit Rajak
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
                    Column {
                        CyberBadge(text = "ADMIN PORTAL", color = NeonCyan)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Admin Dashboard (Ankit Rajak)",
                            color = LightWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Manage syllabus, questions, users & analytics",
                            color = SoftText,
                            fontSize = 11.sp
                        )
                    }
                    Button(
                        onClick = { viewModel.navigateTo(ScreenDestination.AdminPanel) },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                    ) {
                        Text("Access", color = SpaceDark, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // App Brand Footer
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "ANKIT STUDY AI",
                    color = NeonCyan,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Managed by Ankit Rajak",
                    color = LightWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "“Your AI. Your Study. Your Future.”",
                    color = SoftText,
                    fontSize = 11.sp
                )
                Text(
                    text = "Don't just study. Let AI understand how you learn.",
                    color = SoftText,
                    fontSize = 10.sp
                )
            }
        }
    }

    if (showExamDialog) {
        AlertDialog(
            onDismissRequest = { showExamDialog = false },
            title = { Text("Select Target Examination", color = LightWhite, fontWeight = FontWeight.Bold) },
            text = {
                LazyColumn(modifier = Modifier.height(280.dp)) {
                    items(allExams) { exam ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.repository.updateTargetExam(exam)
                                    showExamDialog = false
                                }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = userProfile.targetExam.startsWith(exam.substring(0, 4)),
                                onClick = {
                                    viewModel.repository.updateTargetExam(exam)
                                    showExamDialog = false
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = exam, color = LightWhite, fontSize = 13.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showExamDialog = false }) {
                    Text("Close", color = NeonCyan)
                }
            },
            containerColor = SpaceCardElevated
        )
    }
}

@Composable
fun ProfileStatItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = label, color = SoftText, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, color = color, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}

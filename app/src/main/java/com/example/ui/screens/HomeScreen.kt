package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.models.StudyPlanTask
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenDestination

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.repository.userProfile.collectAsState()
    val examReadiness by viewModel.repository.examReadiness.collectAsState()
    val studyTasks by viewModel.repository.studyPlanTasks.collectAsState()
    val revisionItems by viewModel.repository.revisionRadarItems.collectAsState()
    val currentAffairs by viewModel.repository.currentAffairs.collectAsState()
    var showPlanGeneratorDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceDark)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Good Morning, ${userProfile.name} 👋",
                            color = LightWhite,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Target: ${userProfile.targetExam} • ${userProfile.examDate}",
                        color = SoftText,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "ANKIT STUDY AI • Managed by Ankit Rajak",
                        color = NeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Streak & Level Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(SpaceCard)
                        .border(1.dp, SpaceCardBorder, RoundedCornerShape(12.dp))
                        .clickable { viewModel.navigateTo(ScreenDestination.Profile) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Streak",
                            tint = FlameAmber,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${userProfile.studyStreakDays}d",
                            color = FlameAmber,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = "Coins",
                            tint = HologramGold,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${userProfile.studyCoins}",
                            color = HologramGold,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 4K Ultra HD Hero Command Center Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(176.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .border(1.2.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_home_hero),
                    contentDescription = "ANKIT STUDY AI 4K Command Center",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Cinematic High-Contrast Gradient Scrim
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    SpaceDark.copy(alpha = 0.6f),
                                    SpaceDark.copy(alpha = 0.95f)
                                )
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CyberBadge(text = "ANKIT AI • 4K HD", color = NeonCyan)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "COMMAND CENTER",
                            color = HologramGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.6.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Your AI. Your Study. Your Future.",
                        color = LightWhite,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Deep learning memory & predictive exam readiness",
                        color = SoftText,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Quick Futuristic Actions Pill Row (Vector SVG Icons)
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    QuickActionPill(
                        icon = Icons.AutoMirrored.Filled.EventNote,
                        label = "Study Plan",
                        color = NeonCyan,
                        onClick = { viewModel.navigateTo(ScreenDestination.StudyPlan) }
                    )
                }
                item {
                    QuickActionPill(
                        icon = Icons.AutoMirrored.Filled.MenuBook,
                        label = "Smart Library",
                        color = ElectricBlue,
                        onClick = { viewModel.navigateTo(ScreenDestination.Learn) }
                    )
                }
                item {
                    QuickActionPill(
                        icon = Icons.Default.CameraAlt,
                        label = "Camera Solver",
                        color = NeonCyan,
                        onClick = { viewModel.navigateTo(ScreenDestination.CameraSolver) }
                    )
                }
                item {
                    QuickActionPill(
                        icon = Icons.Default.Mic,
                        label = "Voice AI Teacher",
                        color = CyberViolet,
                        onClick = { viewModel.navigateTo(ScreenDestination.VoiceTeacher) }
                    )
                }
                item {
                    QuickActionPill(
                        icon = Icons.Default.AccountTree,
                        label = "Mind Maps",
                        color = ElectricBlue,
                        onClick = { viewModel.navigateTo(ScreenDestination.MindMap) }
                    )
                }
                item {
                    QuickActionPill(
                        icon = Icons.Default.Timer,
                        label = "Focus Mode",
                        color = HologramGold,
                        onClick = { viewModel.navigateTo(ScreenDestination.FocusMode) }
                    )
                }
                item {
                    QuickActionPill(
                        icon = Icons.Default.AssignmentTurnedIn,
                        label = "Govt Test Series",
                        color = HologramGold,
                        onClick = { viewModel.navigateTo(ScreenDestination.TestSeries) }
                    )
                }
                item {
                    QuickActionPill(
                        icon = Icons.Default.Style,
                        label = "Flashcards",
                        color = CyberGreen,
                        onClick = { viewModel.navigateTo(ScreenDestination.Flashcards) }
                    )
                }
            }
        }

        // Promotional Test Series Launch Banner (@ ₹49 with VIAN12)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF2E1065),
                                Color(0xFF1E1B4B),
                                Color(0xFF0F172A)
                            )
                        )
                    )
                    .border(1.5.dp, HologramGold.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
                    .clickable { viewModel.navigateTo(ScreenDestination.TestSeries) }
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CyberBadge(text = "ALL GOVT EXAMS", color = HologramGold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Token: VIAN12",
                                color = CyberGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Test Series Started from ₹99 → ₹49 Only!",
                            color = LightWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "SSC • Railway • Banking • UPSC • Police • Defence (Max 500 Students)",
                            color = SoftText,
                            fontSize = 11.sp
                        )
                    }

                    Button(
                        onClick = { viewModel.navigateTo(ScreenDestination.TestSeries) },
                        colors = ButtonDefaults.buttonColors(containerColor = HologramGold),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(text = "Claim ₹49", color = SpaceDark, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // AI Exam Readiness Score
        item {
            CircularReadinessGauge(
                scorePercent = examReadiness.overallPercent,
                examName = examReadiness.examName,
                onTap = { viewModel.navigateTo(ScreenDestination.Practice) }
            )
        }

        // Next Best Action Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Brush.horizontalGradient(listOf(SpaceCardElevated, SpaceCard)))
                    .border(1.dp, NeonCyan.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(NeonCyan.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🎯", fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "NEXT BEST ACTION FOR YOU",
                            color = NeonCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = examReadiness.nextBestAction,
                            color = LightWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Signature Revision Radar Preview
        item {
            GlassCard(
                borderColor = AlertRed.copy(alpha = 0.4f),
                modifier = Modifier.clickable { viewModel.navigateTo(ScreenDestination.RevisionRadar) }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "📡", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "SMART REVISION RADAR",
                                color = LightWhite,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "AI detected 3 topics with decaying retention",
                                color = SoftText,
                                fontSize = 11.sp
                            )
                        }
                    }
                    CyberBadge(text = "SIGNATURE AI", color = AlertRed)
                }

                Spacer(modifier = Modifier.height(12.dp))

                revisionItems.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val dotColor = when (item.urgency.label) {
                                item.urgency.label.takeIf { it.contains("High") } -> AlertRed
                                item.urgency.label.takeIf { it.contains("Medium") } -> FlameAmber
                                else -> HologramGold
                            }
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(dotColor)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${item.subject} — ${item.chapter}",
                                color = LightWhite,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Text(
                            text = "${item.retentionScorePercent}% recall",
                            color = SoftText,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                GradientButton(
                    text = "Open Revision Radar & Mini-Test",
                    onClick = { viewModel.navigateTo(ScreenDestination.RevisionRadar) },
                    gradient = Brush.horizontalGradient(listOf(AlertRed, FlameAmber)),
                    contentColor = Color.White,
                    height = 40.dp
                )
            }
        }

        // AI Auto Study Plan
        item {
            GlassCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "AI AUTO STUDY PLAN",
                            color = LightWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Custom schedule generated with Gemini AI",
                            color = SoftText,
                            fontSize = 11.sp
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TextButton(
                            onClick = { showPlanGeneratorDialog = true },
                            modifier = Modifier.testTag("open_gemini_planner_button")
                        ) {
                            Text(text = "✨ Gemini Plan", color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        TextButton(
                            onClick = { viewModel.repository.rearrangeMissedSchedule() },
                            modifier = Modifier.testTag("rearrange_plan_button")
                        ) {
                            Text(text = "⚡ Rearrange", color = HologramGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                studyTasks.forEach { task ->
                    StudyTaskRow(
                        task = task,
                        onToggle = { viewModel.repository.toggleTaskCompletion(task.id) }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.navigateTo(ScreenDestination.StudyPlan) },
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .testTag("open_full_study_plan_screen_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SpaceCardElevated),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f))
                    ) {
                        Text("Trackers & Full Plan →", color = NeonCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { showPlanGeneratorDialog = true },
                        modifier = Modifier
                            .weight(1.2f)
                            .height(40.dp)
                            .testTag("home_gemini_plan_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = SpaceDark, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Gemini AI Plan", color = SpaceDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Daily 10-Minute AI Challenge
        item {
            GlassCard(
                borderColor = CyberViolet.copy(alpha = 0.5f),
                backgroundColor = SpaceCardElevated
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        CyberBadge(text = "TODAY'S 10-MIN SPRINT", color = CyberViolet)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "5 Maths • 5 GK • 5 Reasoning • 1 Concept",
                            color = LightWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Earn +150 XP and 20 Study Coins",
                            color = SoftText,
                            fontSize = 11.sp
                        )
                    }
                    GradientButton(
                        text = "Start",
                        onClick = { viewModel.navigateTo(ScreenDestination.Practice) },
                        gradient = Brush.horizontalGradient(listOf(CyberViolet, ElectricBlue)),
                        contentColor = Color.White,
                        height = 38.dp
                    )
                }
            }
        }

        // Daily Current Affairs AI Briefing
        item {
            GlassCard {
                SectionHeader(
                    title = "Today's Current Affairs AI",
                    subtitle = "Government Schemes • Sci-Tech • Economy",
                    actionText = "All News",
                    onActionClick = { viewModel.navigateTo(ScreenDestination.Learn) }
                )

                currentAffairs.firstOrNull()?.let { article ->
                    Text(
                        text = article.title,
                        color = LightWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = article.summary,
                        color = SoftText,
                        fontSize = 12.sp,
                        maxLines = 2
                    )
                }
            }
        }
    }

    if (showPlanGeneratorDialog) {
        StudyPlanGeneratorDialog(
            viewModel = viewModel,
            onDismiss = { showPlanGeneratorDialog = false }
        )
    }
}

@Composable
fun QuickActionPill(
    icon: ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(SpaceCard)
            .border(1.dp, color.copy(alpha = 0.45f), RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = color,
                    modifier = Modifier.size(15.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                color = LightWhite,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun StudyTaskRow(
    task: StudyPlanTask,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (task.isCompleted) SpaceCard.copy(alpha = 0.4f) else SpaceCardBorder.copy(alpha = 0.2f))
            .clickable(onClick = onToggle)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(checkedColor = NeonCyan)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    text = "${task.subject} — ${task.chapter}",
                    color = if (task.isCompleted) SoftText else LightWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${task.timeSlot} • ${task.goalDescription}",
                    color = if (task.isRescheduled) NeonCyan else SoftText,
                    fontSize = 11.sp
                )
            }
        }

        if (task.isRescheduled) {
            CyberBadge(text = "RE-SCHEDULED", color = NeonCyan)
        }
    }
}

package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.DailyGoal
import com.example.data.models.StudyPlanTask
import com.example.data.models.SubjectProgressTracker
import com.example.ui.components.CyberBadge
import com.example.ui.components.GlassCard
import com.example.ui.components.GradientButton
import com.example.ui.components.StudyPlanGeneratorDialog
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyPlanScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateTo(ScreenDestination.Home)
    }

    val userProfile by viewModel.repository.userProfile.collectAsState()
    val readiness by viewModel.repository.examReadiness.collectAsState()
    val dailyGoals by viewModel.dailyGoals.collectAsState()
    val subjectTrackers by viewModel.subjectProgressTrackers.collectAsState()
    val studyTasks by viewModel.studyPlanTasks.collectAsState()

    var showAiPlannerDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) } // 0: All, 1: Schedule, 2: Goals, 3: Subjects

    if (showAiPlannerDialog) {
        StudyPlanGeneratorDialog(
            viewModel = viewModel,
            onDismiss = { showAiPlannerDialog = false }
        )
    }

    val totalGoals = dailyGoals.size
    val completedGoals = dailyGoals.count { it.isCompleted }
    val completedTasks = studyTasks.count { it.isCompleted }
    val totalTasks = studyTasks.size
    val overallScheduleProgress = if (totalTasks > 0) (completedTasks.toFloat() / totalTasks) else 0f

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceDark)
    ) {
        // Top App Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SpaceCard)
                .border(1.dp, SpaceCardBorder)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.navigateTo(ScreenDestination.Home) },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(SpaceCardElevated)
                            .testTag("study_plan_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = NeonCyan
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "STUDY PLAN & TRACKER",
                                color = LightWhite,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            CyberBadge(text = "AI PACED", color = CyberGreen)
                        }
                        Text(
                            text = "${userProfile.targetExam} • Daily Schedule",
                            color = SoftText,
                            fontSize = 11.sp
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.rearrangeStudySchedule() },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(SpaceCardElevated)
                            .testTag("rearrange_plan_icon_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shuffle,
                            contentDescription = "Rearrange Missed",
                            tint = HologramGold,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { showAiPlannerDialog = true },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier
                            .height(36.dp)
                            .testTag("open_gemini_planner_from_screen")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = SpaceDark,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Gemini Plan",
                            color = SpaceDark,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Filter / Navigation Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = SpaceCard,
            contentColor = NeonCyan,
            edgePadding = 16.dp,
            divider = { HorizontalDivider(color = SpaceCardBorder) }
        ) {
            val tabs = listOf("Overview & All", "Hourly Schedule ($completedTasks/$totalTasks)", "Daily Goals ($completedGoals/$totalGoals)", "Subject Trackers")
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp,
                            color = if (selectedTab == index) NeonCyan else SoftText
                        )
                    }
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Overview Banner Card (Shown on tab 0 or 1)
            if (selectedTab == 0 || selectedTab == 1) {
                item {
                    StudyPlanOverviewCard(
                        userProfileName = userProfile.name,
                        targetExam = userProfile.targetExam,
                        examDate = userProfile.examDate,
                        streakDays = userProfile.studyStreakDays,
                        completedTasks = completedTasks,
                        totalTasks = totalTasks,
                        progressPercent = (overallScheduleProgress * 100).toInt(),
                        onGenerateScheduleClick = { showAiPlannerDialog = true },
                        onRearrangeClick = { viewModel.rearrangeStudySchedule() }
                    )
                }
            }

            // Daily Goals Tracker Section
            if (selectedTab == 0 || selectedTab == 2) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = CyberGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "DAILY GOALS TRACKER",
                                color = LightWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        CyberBadge(
                            text = "$completedGoals/$totalGoals COMPLETED",
                            color = if (completedGoals == totalGoals) CyberGreen else HologramGold
                        )
                    }
                }

                items(dailyGoals) { goal ->
                    DailyGoalCard(
                        goal = goal,
                        onToggle = { viewModel.toggleDailyGoal(goal.id) },
                        onQuickIncrement = { amount -> viewModel.incrementDailyGoal(goal.id, amount) }
                    )
                }
            }

            // Subject Progress Trackers Section
            if (selectedTab == 0 || selectedTab == 3) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "SUBJECT PROGRESS TRACKERS",
                                color = LightWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "${subjectTrackers.size} Core Subjects",
                            color = SoftText,
                            fontSize = 11.sp
                        )
                    }
                }

                items(subjectTrackers) { tracker ->
                    SubjectProgressCard(
                        tracker = tracker,
                        onAddChapter = { viewModel.updateSubjectProgress(tracker.id, 1) },
                        onReviseWeakTopic = { viewModel.navigateTo(ScreenDestination.RevisionRadar) }
                    )
                }
            }

            // Today's Time-Blocked Schedule (Tasks)
            if (selectedTab == 0 || selectedTab == 1) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = HologramGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "TODAY'S HOURLY SCHEDULE",
                                color = LightWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "$completedTasks of $totalTasks Done",
                            color = SoftText,
                            fontSize = 11.sp
                        )
                    }
                }

                items(studyTasks) { task ->
                    StudyTaskCard(
                        task = task,
                        onToggle = { viewModel.toggleStudyPlanTask(task.id) },
                        onStartTimer = { viewModel.navigateTo(ScreenDestination.FocusMode) }
                    )
                }
            }

            // AI Cognitive Insights & Optimization Card
            item {
                GlassCard(
                    borderColor = NeonCyan.copy(alpha = 0.4f),
                    backgroundColor = SpaceCardElevated
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(NeonCyan.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Psychology,
                                    contentDescription = null,
                                    tint = NeonCyan,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "GEMINI COGNITIVE ADVICE",
                                    color = LightWhite,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Adaptive retention & circadian pacing",
                                    color = SoftText,
                                    fontSize = 10.sp
                                )
                            }
                        }
                        CyberBadge(text = "RECOMMENDED", color = CyberGreen)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "• Peak Cognitive Focus Window: 06:00 AM – 09:30 AM (Best for Physics & Math numericals).\n" +
                                "• Afternoon Dip: 02:00 PM – 03:30 PM (Ideal for visual chemistry diagrams & notes reading).\n" +
                                "• Evening Recall Sprint: 08:00 PM – 09:30 PM (Optimal for 25-question timed mock drills).\n" +
                                "• Urgent Revision: Retention in 'Rotational Motion' is decaying. Recommended 45m review today.",
                        color = LightWhite.copy(alpha = 0.9f),
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    GradientButton(
                        text = "✨ Re-Generate Custom Schedule with Gemini AI",
                        onClick = { showAiPlannerDialog = true },
                        gradient = Brush.horizontalGradient(listOf(NeonCyan, ElectricBlue)),
                        contentColor = SpaceDark,
                        height = 42.dp,
                        modifier = Modifier.testTag("gemini_re_generate_button")
                    )
                }
            }
        }
    }
}

@Composable
fun StudyPlanOverviewCard(
    userProfileName: String,
    targetExam: String,
    examDate: String,
    streakDays: Int,
    completedTasks: Int,
    totalTasks: Int,
    progressPercent: Int,
    onGenerateScheduleClick: () -> Unit,
    onRearrangeClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        SpaceCardElevated,
                        SpaceCard
                    )
                )
            )
            .border(1.dp, NeonCyan.copy(alpha = 0.35f), RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    CyberBadge(text = targetExam.uppercase(), color = NeonCyan)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Today's Study Command",
                        color = LightWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Exam Target: $examDate",
                        color = SoftText,
                        fontSize = 11.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(FlameAmber.copy(alpha = 0.15f))
                        .border(1.dp, FlameAmber.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🔥", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$streakDays Days Streak",
                            color = FlameAmber,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Progress Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Daily Schedule Completion: $completedTasks / $totalTasks blocks",
                    color = LightWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "$progressPercent%",
                    color = NeonCyan,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { (progressPercent / 100f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = NeonCyan,
                trackColor = SpaceDark
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onRearrangeClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SpaceCardBorder)
                ) {
                    Icon(
                        imageVector = Icons.Default.Shuffle,
                        contentDescription = null,
                        tint = HologramGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Rearrange Missed",
                        color = LightWhite,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Button(
                    onClick = onGenerateScheduleClick,
                    modifier = Modifier
                        .weight(1.2f)
                        .height(38.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = SpaceDark,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "New AI Plan",
                        color = SpaceDark,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun DailyGoalCard(
    goal: DailyGoal,
    onToggle: () -> Unit,
    onQuickIncrement: (Int) -> Unit
) {
    val progressFraction = (goal.currentValue.toFloat() / goal.targetValue).coerceIn(0f, 1f)
    val categoryIcon = when (goal.category) {
        "focus" -> Icons.Default.Timer
        "questions" -> Icons.Default.Quiz
        "revision" -> Icons.Default.Radar
        "flashcards" -> Icons.Default.Style
        "sprint" -> Icons.Default.Speed
        else -> Icons.Default.CheckCircle
    }
    val categoryColor = when (goal.category) {
        "focus" -> NeonCyan
        "questions" -> ElectricBlue
        "revision" -> AlertRed
        "flashcards" -> CyberGreen
        "sprint" -> HologramGold
        else -> NeonCyan
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(SpaceCard)
            .border(
                1.dp,
                if (goal.isCompleted) CyberGreen.copy(alpha = 0.5f) else SpaceCardBorder,
                RoundedCornerShape(14.dp)
            )
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(categoryColor.copy(alpha = 0.15f))
                            .border(1.dp, categoryColor.copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = categoryIcon,
                            contentDescription = null,
                            tint = categoryColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = goal.title,
                            color = LightWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${goal.currentValue} / ${goal.targetValue} ${goal.unit} • +${goal.xpReward} XP",
                            color = SoftText,
                            fontSize = 11.sp
                        )
                    }
                }

                Checkbox(
                    checked = goal.isCompleted,
                    onCheckedChange = { onToggle() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = CyberGreen,
                        uncheckedColor = SoftText,
                        checkmarkColor = SpaceDark
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { progressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (goal.isCompleted) CyberGreen else categoryColor,
                trackColor = SpaceDark
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${(progressFraction * 100).toInt()}% Met",
                    color = if (goal.isCompleted) CyberGreen else SoftText,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )

                // Quick Increment Action
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    val stepText = when (goal.unit) {
                        "mins" -> "+30m"
                        "questions" -> "+5 Qs"
                        "topics" -> "+1 Top"
                        "cards" -> "+5 Cds"
                        else -> "+1"
                    }
                    val stepAmount = when (goal.unit) {
                        "mins" -> 30
                        "questions" -> 5
                        "topics" -> 1
                        "cards" -> 5
                        else -> 1
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SpaceCardElevated)
                            .border(1.dp, SpaceCardBorder, RoundedCornerShape(6.dp))
                            .clickable { onQuickIncrement(stepAmount) }
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = stepText,
                            color = NeonCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SubjectProgressCard(
    tracker: SubjectProgressTracker,
    onAddChapter: () -> Unit,
    onReviseWeakTopic: () -> Unit
) {
    val themeColor = Color(tracker.colorHex)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SpaceCard)
            .border(1.dp, themeColor.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column {
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
                            .background(themeColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = tracker.subjectName,
                        color = LightWhite,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                CyberBadge(
                    text = "${tracker.syllabusPercent}% SYLLABUS",
                    color = themeColor
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Metrics Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Chapters", color = SoftText, fontSize = 10.sp)
                    Text(
                        text = "${tracker.completedChapters} / ${tracker.totalChapters}",
                        color = LightWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column {
                    Text(text = "Test Accuracy", color = SoftText, fontSize = 10.sp)
                    Text(
                        text = "${tracker.accuracyPercent}%",
                        color = CyberGreen,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column {
                    Text(text = "Study Time", color = SoftText, fontSize = 10.sp)
                    Text(
                        text = "${tracker.weeklyHoursSpent} hrs/wk",
                        color = HologramGold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { (tracker.syllabusPercent / 100f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = themeColor,
                trackColor = SpaceDark
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Weak Topic Alert Pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(SpaceDark)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "⚠️", fontSize = 11.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Weak Topic: ${tracker.weakTopic}",
                            color = AlertRed,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "⭐ Strong: ${tracker.strongTopic}",
                        color = SoftText,
                        fontSize = 10.sp
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(AlertRed.copy(alpha = 0.2f))
                            .border(1.dp, AlertRed.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                            .clickable { onReviseWeakTopic() }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Revise",
                            color = AlertRed,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(themeColor.copy(alpha = 0.2f))
                            .border(1.dp, themeColor.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                            .clickable { onAddChapter() }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "+1 Chapter",
                            color = themeColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StudyTaskCard(
    task: StudyPlanTask,
    onToggle: () -> Unit,
    onStartTimer: () -> Unit
) {
    val subjectColor = when {
        task.subject.contains("Physics", ignoreCase = true) -> NeonCyan
        task.subject.contains("Math", ignoreCase = true) -> ElectricBlue
        task.subject.contains("Chem", ignoreCase = true) -> CyberGreen
        task.subject.contains("Radar", ignoreCase = true) -> AlertRed
        else -> HologramGold
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(SpaceCard)
            .border(
                1.dp,
                if (task.isCompleted) CyberGreen.copy(alpha = 0.5f) else SpaceCardBorder,
                RoundedCornerShape(14.dp)
            )
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    CyberBadge(text = task.subject, color = subjectColor)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = task.timeSlot,
                        color = HologramGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (task.isRescheduled) {
                        Spacer(modifier = Modifier.width(6.dp))
                        CyberBadge(text = "RE-SCHEDULED", color = AlertRed)
                    }
                }

                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onToggle() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = CyberGreen,
                        uncheckedColor = SoftText,
                        checkmarkColor = SpaceDark
                    )
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = task.chapter,
                color = LightWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = task.goalDescription,
                color = SoftText,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = SoftText,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${task.durationMinutes} Minutes",
                        color = SoftText,
                        fontSize = 11.sp
                    )
                }

                if (!task.isCompleted) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(NeonCyan.copy(alpha = 0.15f))
                            .border(1.dp, NeonCyan.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .clickable { onStartTimer() }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Focus Timer",
                                color = NeonCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = CyberGreen,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Completed (+35 XP)",
                            color = CyberGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

package com.example.ui.components

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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.models.StudyPlanTask
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

@Composable
fun StudyPlanGeneratorDialog(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val isGenerating by viewModel.isAiGeneratingPlan.collectAsState()
    val previewSchedule by viewModel.aiPlanPreview.collectAsState()
    val userProfile by viewModel.repository.userProfile.collectAsState()

    var selectedExam by remember { mutableStateOf(userProfile.targetExam) }
    var selectedHours by remember { mutableStateOf(6) }
    var selectedLevel by remember { mutableStateOf("Intermediate") }
    var includeRevision by remember { mutableStateOf(true) }
    var includeMockSprint by remember { mutableStateOf(true) }

    val examOptions = listOf(
        "JEE Main & Advanced",
        "NEET (Medical)",
        "UPSC Civil Services",
        "SSC CGL / CHSL",
        "Banking (IBPS / SBI PO)",
        "Railway (RRB NTPC)",
        "State PSC",
        "Defence (NDA / CDS)"
    )

    val prepLevels = listOf("Beginner", "Intermediate", "Advanced / Revision")
    val hourOptions = listOf(2, 4, 6, 8, 10)

    val allSubjectChips = listOf(
        "Physics",
        "Chemistry",
        "Mathematics",
        "Biology",
        "Quantitative Aptitude",
        "Reasoning",
        "General Studies",
        "Current Affairs"
    )
    val selectedSubjects = remember { mutableStateListOf("Physics", "Mathematics") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.9f)
                .clip(RoundedCornerShape(20.dp))
                .border(1.5.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(20.dp)),
            color = SpaceCardElevated
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // Header
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
                                .background(NeonCyan.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "✨", fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "GEMINI AI STUDY PLANNER",
                                color = LightWhite,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "Powered by Gemini 3.5 Flash • Adaptive Cognitive Pacing",
                                color = NeonCyan,
                                fontSize = 10.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(SpaceCard)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = SoftText)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = SpaceCardBorder)
                Spacer(modifier = Modifier.height(10.dp))

                if (previewSchedule == null) {
                    // Configuration Form
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // 1. Target Exam Goal
                        item {
                            Text(text = "1. Target Examination Goal", color = LightWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(examOptions) { exam ->
                                    val isSelected = selectedExam == exam
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(if (isSelected) NeonCyan else SpaceCard)
                                            .border(1.dp, if (isSelected) NeonCyan else SpaceCardBorder, RoundedCornerShape(10.dp))
                                            .clickable { selectedExam = exam }
                                            .padding(horizontal = 12.dp, vertical = 7.dp)
                                    ) {
                                        Text(
                                            text = exam,
                                            color = if (isSelected) SpaceDark else LightWhite,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        // 2. Available Daily Hours
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "2. Available Daily Study Time", color = LightWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text(text = "$selectedHours Hours / Day", color = HologramGold, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                hourOptions.forEach { hrs ->
                                    val isSelected = selectedHours == hrs
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) HologramGold else SpaceCard)
                                            .border(1.dp, if (isSelected) HologramGold else SpaceCardBorder, RoundedCornerShape(8.dp))
                                            .clickable { selectedHours = hrs }
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${hrs}h",
                                            color = if (isSelected) SpaceDark else LightWhite,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        // 3. Preparation Stage
                        item {
                            Text(text = "3. Current Preparation Stage", color = LightWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                prepLevels.forEach { level ->
                                    val isSelected = selectedLevel == level
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) CyberViolet else SpaceCard)
                                            .border(1.dp, if (isSelected) CyberViolet else SpaceCardBorder, RoundedCornerShape(8.dp))
                                            .clickable { selectedLevel = level }
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = level,
                                            color = if (isSelected) Color.White else SoftText,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        // 4. Focus / Weak Subjects
                        item {
                            Text(text = "4. Select Focus / Weak Subjects", color = LightWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(allSubjectChips) { subj ->
                                    val isSelected = selectedSubjects.contains(subj)
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) ElectricBlue else SpaceCard)
                                            .border(1.dp, if (isSelected) NeonCyan else SpaceCardBorder, RoundedCornerShape(8.dp))
                                            .clickable {
                                                if (isSelected) selectedSubjects.remove(subj) else selectedSubjects.add(subj)
                                            }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = if (isSelected) "✓ $subj" else "+ $subj",
                                            color = if (isSelected) Color.White else SoftText,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }

                        // 5. Smart Inclusion Toggles
                        item {
                            Text(text = "5. Pedagogical Blocks", color = LightWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SpaceCard)
                                    .clickable { includeRevision = !includeRevision }
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(text = "Include Spaced Revision Block", color = LightWhite, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                    Text(text = "Allocates 45m for Revision Radar & Mistake Book", color = SoftText, fontSize = 10.sp)
                                }
                                Switch(
                                    checked = includeRevision,
                                    onCheckedChange = { includeRevision = it },
                                    colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan)
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SpaceCard)
                                    .clickable { includeMockSprint = !includeMockSprint }
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(text = "Include Timed Mock Sprint", color = LightWhite, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                    Text(text = "Allocates timed exam testing session", color = SoftText, fontSize = 10.sp)
                                }
                                Switch(
                                    checked = includeMockSprint,
                                    onCheckedChange = { includeMockSprint = it },
                                    colors = SwitchDefaults.colors(checkedThumbColor = HologramGold)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Generation Action Button
                    Button(
                        onClick = {
                            viewModel.generateCustomAiSchedule(
                                examGoal = selectedExam,
                                hours = selectedHours,
                                prepLevel = selectedLevel,
                                subjects = selectedSubjects.toList(),
                                includeRevision = includeRevision,
                                includeMockSprint = includeMockSprint
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("generate_schedule_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                        enabled = !isGenerating
                    ) {
                        if (isGenerating) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = SpaceDark)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = "Gemini AI is crafting your schedule...", color = SpaceDark, fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = SpaceDark, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Generate Custom Schedule with Gemini", color = SpaceDark, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                } else {
                    // Preview of Generated Schedule
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                CyberBadge(text = "AI GENERATED SCHEDULE", color = CyberGreen)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "$selectedExam • $selectedHours Hours",
                                    color = LightWhite,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "${previewSchedule!!.size} Time Blocks",
                                color = HologramGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(previewSchedule!!) { task ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(SpaceCard)
                                        .border(1.dp, SpaceCardBorder, RoundedCornerShape(10.dp))
                                        .padding(10.dp)
                                ) {
                                    Column {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                CyberBadge(text = task.subject, color = NeonCyan)
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(text = task.chapter, color = LightWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            }
                                            Text(text = "⏱️ ${task.durationMinutes}m", color = SoftText, fontSize = 11.sp)
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = "${task.timeSlot} • ${task.goalDescription}", color = SoftText, fontSize = 11.sp)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { viewModel.dismissAiPlanPreview() },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SpaceCardBorder)
                            ) {
                                Text("Re-Configure", color = LightWhite, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                            }

                            Button(
                                onClick = {
                                    viewModel.applyAiScheduleToToday()
                                    onDismiss()
                                },
                                modifier = Modifier
                                    .weight(1.5f)
                                    .height(48.dp)
                                    .testTag("apply_schedule_button"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = CyberGreen)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = SpaceDark, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Apply to Today's Plan", color = SpaceDark, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.data.models.MistakeCategory
import com.example.data.models.PracticeQuestion
import com.example.ui.components.CyberBadge
import com.example.ui.components.GlassCard
import com.example.ui.components.GradientButton
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenDestination

@Composable
fun PracticeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val mistakeList by viewModel.repository.mistakeBook.collectAsState()
    var selectedTab by remember { mutableStateOf(0) } // 0: AI Question Generator, 1: My Mistake Book, 2: Daily Challenge
    var selectedDifficulty by remember { mutableStateOf("Medium") }
    var selectedSubject by remember { mutableStateOf("Physics") }

    // Active practice question state
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedAnswerIndex by remember { mutableStateOf<Int?>(null) }
    var answerSubmitted by remember { mutableStateOf(false) }

    val sampleQuestions = remember {
        listOf(
            PracticeQuestion(
                id = "pq1",
                subject = "Physics",
                chapter = "Rotational Motion",
                difficulty = "Medium",
                questionText = "A solid cylinder and a hollow cylinder of equal mass and radius roll down an inclined plane without slipping. Which reaches the bottom first?",
                options = listOf("Solid cylinder", "Hollow cylinder", "Both at the same time", "Depends on incline angle"),
                correctIndex = 0,
                explanation = "Acceleration a = g sinθ / (1 + I / mR²). For solid cylinder, I/(mR²) = 1/2 = 0.5. For hollow, I/(mR²) = 1. Smaller moment of inertia ratio means higher linear acceleration, reaching the bottom first.",
                stepByStep = "1. Write energy conservation or torque equation.\n2. a = g sinθ / (1 + k²/R²).\n3. Solid cylinder has k²/R² = 1/2.\n4. Hollow cylinder has k²/R² = 1.\n5. Therefore a_solid > a_hollow.",
                anotherMethod = "Intuition: Solid cylinder has mass closer to the axis, so less energy is stolen into rotation, leaving more for translation speed!",
                realLifeExample = "Race between a solid marble and a hollow metal ring down a ramp."
            ),
            PracticeQuestion(
                id = "pq2",
                subject = "Mathematics",
                chapter = "Calculus",
                difficulty = "Hard",
                questionText = "If f(x) = |x - 1| + |x - 2|, then for what value of x does f(x) achieve its minimum?",
                options = listOf("x = 1 only", "x = 2 only", "All x in [1, 2]", "x = 1.5 only"),
                correctIndex = 2,
                explanation = "In the interval [1, 2], |x - 1| = x - 1 and |x - 2| = 2 - x. Their sum is (x - 1) + (2 - x) = 1, which is a flat minimum of 1 everywhere across [1, 2].",
                stepByStep = "1. Break into regions: x < 1, 1 ≤ x ≤ 2, x > 2.\n2. In [1, 2], f(x) = (x - 1) + (2 - x) = 1 constant.\n3. Outside, f(x) > 1.\n4. Hence f(x) is minimum on the entire closed interval [1, 2].",
                anotherMethod = "Geometric: |x - a| + |x - b| is total distance from x to points a and b on number line. Min distance is distance between 1 and 2 = 1.",
                realLifeExample = "Optimal logistics warehouse location along a highway between two delivery hubs."
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceDark)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "AI PRACTICE & MISTAKES",
                    color = LightWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Adaptive Questions • Mistake Book • Sprints",
                    color = SoftText,
                    fontSize = 12.sp
                )
            }
            CyberBadge(text = "UNLIMITED", color = NeonCyan)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = SpaceDark,
            contentColor = NeonCyan,
            divider = {}
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("⚡ AI Generator", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("📝 Mistake Book (${mistakeList.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("🏆 Daily Sprint", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
            Tab(
                selected = false,
                onClick = { viewModel.navigateTo(ScreenDestination.TestSeries) },
                text = { Text("🎯 Govt Tests (₹49)", fontWeight = FontWeight.Bold, color = HologramGold, fontSize = 11.sp) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (selectedTab) {
            0 -> {
                // AI Question Generator
                Column {
                    // Difficulty Selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Difficulty:", color = SoftText, fontSize = 12.sp)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("Easy", "Medium", "Hard", "Expert").forEach { diff ->
                                val isSelected = selectedDifficulty == diff
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) NeonCyan else SpaceCard)
                                        .border(1.dp, if (isSelected) NeonCyan else SpaceCardBorder, RoundedCornerShape(8.dp))
                                        .clickable { selectedDifficulty = diff }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = diff,
                                        color = if (isSelected) SpaceDark else LightWhite,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val activeQ = sampleQuestions[currentQuestionIndex % sampleQuestions.size]

                    GlassCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CyberBadge(text = "${activeQ.subject.uppercase()} • ${activeQ.difficulty}", color = NeonCyan)
                            Text(text = "Q ${currentQuestionIndex + 1}/${sampleQuestions.size}", color = SoftText, fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = activeQ.questionText,
                            color = LightWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        activeQ.options.forEachIndexed { idx, opt ->
                            val isSelected = selectedAnswerIndex == idx
                            val bg = when {
                                answerSubmitted && idx == activeQ.correctIndex -> CyberGreen.copy(alpha = 0.25f)
                                answerSubmitted && isSelected && idx != activeQ.correctIndex -> AlertRed.copy(alpha = 0.25f)
                                isSelected -> ElectricBlue.copy(alpha = 0.3f)
                                else -> SpaceDark
                            }
                            val borderCol = when {
                                answerSubmitted && idx == activeQ.correctIndex -> CyberGreen
                                answerSubmitted && isSelected && idx != activeQ.correctIndex -> AlertRed
                                isSelected -> NeonCyan
                                else -> SpaceCardBorder
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(bg)
                                    .border(1.dp, borderCol, RoundedCornerShape(10.dp))
                                    .clickable(enabled = !answerSubmitted) { selectedAnswerIndex = idx }
                                    .padding(12.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${('A' + idx)}. ",
                                        color = NeonCyan,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(text = opt, color = LightWhite, fontSize = 13.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        if (!answerSubmitted && selectedAnswerIndex != null) {
                            GradientButton(
                                text = "Check Answer",
                                onClick = {
                                    answerSubmitted = true
                                    if (selectedAnswerIndex == activeQ.correctIndex) {
                                        viewModel.repository.awardXp(30)
                                    }
                                }
                            )
                        }

                        if (answerSubmitted) {
                            Spacer(modifier = Modifier.height(8.dp))
                            val isCorrect = selectedAnswerIndex == activeQ.correctIndex
                            Text(
                                text = if (isCorrect) "🎉 Correct! +30 XP" else "❌ Incorrect. Correct Option: ${('A' + activeQ.correctIndex)}",
                                color = if (isCorrect) CyberGreen else AlertRed,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = activeQ.explanation, color = SoftText, fontSize = 12.sp, lineHeight = 17.sp)

                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                TextButton(onClick = {
                                    viewModel.sendChatQuery("Explain this question simply: ${activeQ.questionText}", mode = "simple")
                                }) {
                                    Text("Ask ANKIT AI", color = NeonCyan, fontSize = 12.sp)
                                }

                                Button(
                                    onClick = {
                                        currentQuestionIndex++
                                        selectedAnswerIndex = null
                                        answerSubmitted = false
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                                ) {
                                    Text("Next Question →", color = SpaceDark, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // My Mistake Book
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Auto-Categorized Mistake Library",
                            color = SoftText,
                            fontSize = 12.sp
                        )
                        Button(
                            onClick = { selectedTab = 0 },
                            colors = ButtonDefaults.buttonColors(containerColor = AlertRed),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text("⚡ Mistake Test", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 96.dp)
                    ) {
                        items(mistakeList) { mistake ->
                            GlassCard(
                                borderColor = when (mistake.category) {
                                    MistakeCategory.CONCEPT -> CyberViolet
                                    MistakeCategory.CALCULATION -> HologramGold
                                    MistakeCategory.MEMORY -> FlameAmber
                                    MistakeCategory.SILLY -> AlertRed
                                    MistakeCategory.TIME_MANAGEMENT -> ElectricBlue
                                }.copy(alpha = 0.5f)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    CyberBadge(
                                        text = "${mistake.category.icon} ${mistake.category.label}",
                                        color = when (mistake.category) {
                                            MistakeCategory.CONCEPT -> CyberViolet
                                            MistakeCategory.CALCULATION -> HologramGold
                                            MistakeCategory.MEMORY -> FlameAmber
                                            MistakeCategory.SILLY -> AlertRed
                                            MistakeCategory.TIME_MANAGEMENT -> ElectricBlue
                                        }
                                    )
                                    Text(text = mistake.subject, color = SoftText, fontSize = 11.sp)
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = mistake.questionText,
                                    color = LightWhite,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Your slip: ${mistake.studentAnswer}",
                                    color = AlertRed,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "Correct: ${mistake.correctAnswer}",
                                    color = CyberGreen,
                                    fontSize = 12.sp
                                )

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = mistake.aiExplanation,
                                    color = SoftText,
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }

            2 -> {
                // Daily Challenge Sprint
                GlassCard(
                    borderColor = HologramGold.copy(alpha = 0.5f),
                    backgroundColor = SpaceCardElevated
                ) {
                    CyberBadge(text = "10-MINUTE DAILY SPRINT", color = HologramGold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Today's Balanced Cognitive Challenge",
                        color = LightWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "5 Mathematics • 5 General Knowledge • 5 Logical Reasoning • 1 Core Concept Challenge",
                        color = SoftText,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "16", color = LightWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Questions", color = SoftText, fontSize = 11.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "10:00", color = HologramGold, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Duration", color = SoftText, fontSize = 11.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "+150 XP", color = CyberGreen, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Reward", color = SoftText, fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    GradientButton(
                        text = "Start Today's Sprint",
                        onClick = { selectedTab = 0 },
                        gradient = Brush.horizontalGradient(listOf(HologramGold, FlameAmber)),
                        contentColor = SpaceDark
                    )
                }
            }
        }
    }
}

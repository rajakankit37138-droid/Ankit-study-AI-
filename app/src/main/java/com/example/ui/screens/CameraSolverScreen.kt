package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CropFree
import androidx.compose.material.icons.filled.FlashOn
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
import com.example.ui.components.CyberBadge
import com.example.ui.components.GlassCard
import com.example.ui.components.GradientButton
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenDestination

@Composable
fun CameraSolverScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler { viewModel.navigateTo(ScreenDestination.Home) }

    val currentProblem by viewModel.cameraProblemSample.collectAsState()
    val solutionResult by viewModel.cameraSolutionResult.collectAsState()
    val isThinking by viewModel.isAiThinking.collectAsState()
    var editableProblem by remember { mutableStateOf(currentProblem) }

    val sampleProblems = listOf(
        "A 5 kg block is pulled with 20 N force at 30° angle. Find acceleration.",
        "Evaluate indefinite integral: ∫ sin(x) / (1 + cos²(x)) dx",
        "Explain hybridization and magnetic behavior of [Fe(CN)6]³⁻ complex ion.",
        "A train 150m long crosses a 250m bridge in 20 seconds. Find speed in km/h."
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
                    text = "AI CAMERA DOUBT SOLVER",
                    color = LightWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Instant OCR • Step-by-Step • Alternate Shortcuts",
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
            // Camera Viewfinder Simulation Box
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(SpaceCardElevated)
                        .border(1.5.dp, NeonCyan.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CyberBadge(text = "SCANNING VIEWPORT", color = NeonCyan)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.FlashOn, contentDescription = null, tint = HologramGold, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(Icons.Default.CropFree, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(18.dp))
                            }
                        }

                        // Scanning animation line simulation & text
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Point at handwritten or printed question",
                                color = LightWhite,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Maths • Physics • Chemistry • Biology • Reasoning • GK",
                                color = SoftText,
                                fontSize = 11.sp
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(text = "✓ Auto-crop & high contrast enabled", color = CyberGreen, fontSize = 10.sp)
                        }
                    }
                }
            }

            // Quick Samples Row
            item {
                Text(text = "Or pick a sample scanned question:", color = SoftText, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(sampleProblems) { sample ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(SpaceCard)
                                .border(1.dp, SpaceCardBorder, RoundedCornerShape(10.dp))
                                .clickable {
                                    editableProblem = sample
                                    viewModel.solveCameraProblem(sample)
                                }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = sample.take(28) + "...",
                                color = NeonCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Editable Question Input & Scan Button
            item {
                GlassCard {
                    Text(text = "Scanned Question Text:", color = LightWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = editableProblem,
                        onValueChange = { editableProblem = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                            .testTag("scanned_problem_text"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = SpaceCardBorder,
                            focusedTextColor = LightWhite,
                            unfocusedTextColor = LightWhite
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    GradientButton(
                        text = if (isThinking) "AI is Solving..." else "⚡ Solve with ANKIT AI",
                        onClick = { viewModel.solveCameraProblem(editableProblem) },
                        gradient = Brush.horizontalGradient(listOf(NeonCyan, ElectricBlue)),
                        contentColor = SpaceDark
                    )
                }
            }

            // Solution Result
            if (isThinking) {
                item {
                    GlassCard {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = NeonCyan)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = "Identifying formulas & constraints...", color = LightWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text(text = "ANKIT AI is calculating step-by-step breakdown", color = SoftText, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            solutionResult?.let { sol ->
                item {
                    GlassCard(borderColor = CyberGreen.copy(alpha = 0.6f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CyberBadge(text = "AI SOLUTION VERIFIED", color = CyberGreen)
                            Text(text = "+35 XP Earned", color = HologramGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = sol,
                            color = LightWhite,
                            fontSize = 13.sp,
                            lineHeight = 19.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = {
                                viewModel.sendChatQuery("Teach me another method for: $editableProblem", mode = "another_method")
                                viewModel.navigateTo(ScreenDestination.AnkitAi)
                            }) {
                                Text("Ask Alternate Trick in Chat →", color = NeonCyan, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

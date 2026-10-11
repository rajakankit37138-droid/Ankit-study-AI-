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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.MindMapNode
import com.example.ui.components.CyberBadge
import com.example.ui.components.GlassCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenDestination

@Composable
fun MindMapScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler { viewModel.navigateTo(ScreenDestination.Home) }

    val activeMindMap by viewModel.activeMindMap.collectAsState()
    val availableChapters = listOf(
        "Laws of Motion",
        "Electrostatics",
        "Thermodynamics",
        "Chemical Bonding",
        "Indian Constitution"
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
                    text = "AI MIND MAP GENERATOR",
                    color = LightWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Chapter → Concepts → Formulas → Examples → PYQ",
                    color = SoftText,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Chapter Selection Bar
        Text(text = "Select Chapter to Map:", color = SoftText, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(availableChapters) { ch ->
                val isSelected = activeMindMap.title.contains(ch, ignoreCase = true)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) NeonCyan else SpaceCard)
                        .border(1.dp, if (isSelected) NeonCyan else SpaceCardBorder, RoundedCornerShape(10.dp))
                        .clickable { viewModel.loadMindMap(ch) }
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = ch,
                        color = if (isSelected) SpaceDark else LightWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mind Map Tree Viewer
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            // Root Chapter Card
            item {
                GlassCard(
                    borderColor = NeonCyan,
                    backgroundColor = SpaceCardElevated
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CyberBadge(text = "CHAPTER ROOT", color = NeonCyan)
                        Text(text = "🧠 Neural Map", color = HologramGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = activeMindMap.title,
                        color = LightWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = activeMindMap.subtitle,
                        color = SoftText,
                        fontSize = 12.sp
                    )
                }
            }

            // Concepts, Formulas & Examples
            items(activeMindMap.children) { conceptNode ->
                MindMapConceptItem(conceptNode = conceptNode)
            }
        }
    }
}

@Composable
fun MindMapConceptItem(conceptNode: MindMapNode) {
    var isExpanded by remember { mutableStateOf(true) }

    GlassCard(
        borderColor = CyberViolet.copy(alpha = 0.5f),
        modifier = Modifier.padding(start = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                CyberBadge(text = "CONCEPT NODE", color = CyberViolet)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = conceptNode.title,
                    color = LightWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = conceptNode.subtitle,
                    color = SoftText,
                    fontSize = 11.sp
                )
            }
            Text(
                text = if (isExpanded) "▲" else "▼",
                color = NeonCyan,
                fontSize = 14.sp
            )
        }

        if (isExpanded && conceptNode.children.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = SpaceCardBorder)
            Spacer(modifier = Modifier.height(10.dp))

            conceptNode.children.forEach { child ->
                val (badgeColor, typeLabel) = when (child.nodeType) {
                    "Formula" -> HologramGold to "FORMULA"
                    "Example" -> CyberGreen to "EXAMPLE"
                    "HighYield" -> AlertRed to "HIGH-YIELD"
                    else -> ElectricBlue to child.nodeType.uppercase()
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SpaceDark)
                        .border(1.dp, badgeColor.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CyberBadge(text = typeLabel, color = badgeColor)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = child.title,
                                color = LightWhite,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = child.subtitle,
                            color = SoftText,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

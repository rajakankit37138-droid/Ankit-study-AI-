package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(16.dp),
    borderColor: Color = SpaceCardBorder,
    backgroundColor: Color = SpaceCard,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, borderColor), shape),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
fun CyberBadge(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = NeonCyan,
    backgroundColor: Color = color.copy(alpha = 0.15f)
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun CircularReadinessGauge(
    scorePercent: Int,
    examName: String,
    modifier: Modifier = Modifier,
    onTap: () -> Unit = {}
) {
    val animatedProgress by animateFloatAsState(
        targetValue = scorePercent / 100f,
        animationSpec = tween(durationMillis = 1000),
        label = "readiness_gauge"
    )

    GlassCard(
        modifier = modifier
            .clickable { onTap() }
            .testTag("exam_readiness_card"),
        borderColor = NeonCyan.copy(alpha = 0.35f),
        backgroundColor = SpaceCardElevated
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                CyberBadge(text = "AI EXAM READINESS", color = NeonCyan)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = examName,
                    color = LightWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Dynamic AI calculation from 7 metrics",
                    color = SoftText,
                    fontSize = 12.sp
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(80.dp)
            ) {
                CircularProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.size(76.dp),
                    color = SpaceCardBorder,
                    strokeWidth = 7.dp,
                    trackColor = Color.Transparent
                )
                CircularProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.size(76.dp),
                    color = when {
                        scorePercent >= 75 -> NeonCyan
                        scorePercent >= 50 -> HologramGold
                        else -> AlertRed
                    },
                    strokeWidth = 7.dp,
                    trackColor = Color.Transparent
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$scorePercent%",
                        color = LightWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "READY",
                        color = SoftText,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    gradient: Brush = Brush.horizontalGradient(listOf(NeonCyan, ElectricBlue)),
    contentColor: Color = SpaceDark,
    height: Dp = 48.dp
) {
    Box(
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(12.dp))
            .background(gradient)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                color = contentColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun StatPill(
    iconText: String,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    accentColor: Color = NeonCyan
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SpaceCard)
            .border(1.dp, SpaceCardBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = iconText, fontSize = 18.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = label, color = SoftText, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                Text(text = value, color = accentColor, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    actionText: String? = null,
    onActionClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                color = LightWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    color = SoftText,
                    fontSize = 12.sp
                )
            }
        }
        if (actionText != null) {
            TextButton(onClick = onActionClick) {
                Text(
                    text = actionText,
                    color = NeonCyan,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

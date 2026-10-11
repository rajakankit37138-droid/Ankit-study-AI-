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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.models.ChatMessage
import com.example.ui.components.CyberBadge
import com.example.ui.components.GlassCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnkitAiScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.chatMessages.collectAsState()
    val isThinking by viewModel.isAiThinking.collectAsState()
    val userProfile by viewModel.repository.userProfile.collectAsState()
    var inputText by remember { mutableStateOf("") }
    var selectedLanguage by remember { mutableStateOf(userProfile.preferredLanguage) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceDark)
    ) {
        // Top Tutor Bar
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
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, NeonCyan, CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_ai_tutor_avatar),
                            contentDescription = "ANKIT AI Tutor Avatar",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "ANKIT AI TUTOR",
                                color = LightWhite,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            CyberBadge(text = "ONLINE", color = CyberGreen)
                        }
                        Text(
                            text = "Managed by Ankit Rajak • AI 24/7",
                            color = SoftText,
                            fontSize = 11.sp
                        )
                    }
                }

                // Language Selector
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SpaceDark)
                        .padding(4.dp)
                ) {
                    listOf("Hinglish", "Hindi", "English").forEach { lang ->
                        val isSelected = selectedLanguage == lang
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) NeonCyan else Color.Transparent)
                                .clickable {
                                    selectedLanguage = lang
                                    viewModel.repository.updateLanguage(lang)
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = lang,
                                color = if (isSelected) SpaceDark else SoftText,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Quick Pedagogical Suggestion Pills
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(SpaceDark)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listOf("Explain Simply", "Step-by-Step", "Another Method", "Give Example", "Test Me")) { pill ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(SpaceCard)
                        .border(1.dp, SpaceCardBorder, RoundedCornerShape(20.dp))
                        .clickable { viewModel.sendQuickAction(pill, selectedLanguage) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = pill,
                        color = NeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Chat Conversation List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(messages) { msg ->
                ChatMessageItem(
                    message = msg,
                    onQuickAction = { action -> viewModel.sendQuickAction(action, selectedLanguage) }
                )
            }

            if (isThinking) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = NeonCyan,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ANKIT AI is analyzing your learning style...",
                            color = NeonCyan,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Bottom Input Dock
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SpaceCard)
                .border(1.dp, SpaceCardBorder)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Camera Button
                IconButton(
                    onClick = { viewModel.navigateTo(ScreenDestination.CameraSolver) },
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Camera Question",
                        tint = NeonCyan
                    )
                }

                // Voice Button
                IconButton(
                    onClick = { viewModel.navigateTo(ScreenDestination.VoiceTeacher) },
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Voice Tutor",
                        tint = CyberViolet
                    )
                }

                // Text Input
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            text = "Ask question in $selectedLanguage...",
                            color = SoftText,
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("tutor_input_field"),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = SpaceDark,
                        unfocusedContainerColor = SpaceDark,
                        focusedTextColor = LightWhite,
                        unfocusedTextColor = LightWhite,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Send Button
                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            viewModel.sendChatQuery(inputText, language = selectedLanguage)
                            inputText = ""
                        }
                    },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(NeonCyan)
                        .testTag("send_query_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = SpaceDark
                    )
                }
            }
        }
    }
}

@Composable
fun ChatMessageItem(
    message: ChatMessage,
    onQuickAction: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!message.isUser) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .border(1.dp, NeonCyan, CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_ai_tutor_avatar),
                    contentDescription = "ANKIT AI",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            modifier = Modifier.widthIn(max = 310.dp),
            horizontalAlignment = if (message.isUser) Alignment.End else Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (message.isUser) 16.dp else 4.dp,
                            bottomEnd = if (message.isUser) 4.dp else 16.dp
                        )
                    )
                    .background(if (message.isUser) ElectricBlue else SpaceCard)
                    .border(
                        1.dp,
                        if (message.isUser) ElectricBlue else SpaceCardBorder,
                        RoundedCornerShape(16.dp)
                    )
                    .padding(14.dp)
            ) {
                Text(
                    text = message.text,
                    color = LightWhite,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }

            // Quick actions on AI message
            if (!message.isUser && message.quickActions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(message.quickActions) { action ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(SpaceCardElevated)
                                .border(1.dp, SpaceCardBorder, RoundedCornerShape(12.dp))
                                .clickable { onQuickAction(action) }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = action,
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
}

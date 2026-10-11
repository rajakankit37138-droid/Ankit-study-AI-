package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenDestination

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnkitStudyAiTheme(darkTheme = true) {
                MainScreenContainer(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainScreenContainer(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()

    val isTopLevelScreen = currentScreen is ScreenDestination.Home ||
            currentScreen is ScreenDestination.Learn ||
            currentScreen is ScreenDestination.AnkitAi ||
            currentScreen is ScreenDestination.Practice ||
            currentScreen is ScreenDestination.Profile

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(SpaceDark),
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            if (isTopLevelScreen) {
                NavigationBar(
                    containerColor = SpaceCard,
                    contentColor = NeonCyan,
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .border(1.dp, SpaceCardBorder)
                        .testTag("bottom_nav_bar")
                ) {
                    NavItem(
                        label = "Home",
                        icon = Icons.Default.Home,
                        isSelected = currentScreen is ScreenDestination.Home,
                        onClick = { viewModel.navigateTo(ScreenDestination.Home) }
                    )
                    NavItem(
                        label = "Learn",
                        icon = Icons.AutoMirrored.Filled.MenuBook,
                        isSelected = currentScreen is ScreenDestination.Learn,
                        onClick = { viewModel.navigateTo(ScreenDestination.Learn) }
                    )
                    NavItem(
                        label = "ANKIT AI",
                        icon = Icons.Default.AutoAwesome,
                        isSelected = currentScreen is ScreenDestination.AnkitAi,
                        onClick = { viewModel.navigateTo(ScreenDestination.AnkitAi) },
                        isSpecial = true
                    )
                    NavItem(
                        label = "Practice",
                        icon = Icons.Default.Quiz,
                        isSelected = currentScreen is ScreenDestination.Practice,
                        onClick = { viewModel.navigateTo(ScreenDestination.Practice) }
                    )
                    NavItem(
                        label = "Profile",
                        icon = Icons.Default.Person,
                        isSelected = currentScreen is ScreenDestination.Profile,
                        onClick = { viewModel.navigateTo(ScreenDestination.Profile) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(SpaceDark)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen_transition"
            ) { target ->
                when (target) {
                    ScreenDestination.Home -> HomeScreen(viewModel = viewModel)
                    ScreenDestination.Learn -> LearnScreen(viewModel = viewModel)
                    ScreenDestination.AnkitAi -> AnkitAiScreen(viewModel = viewModel)
                    ScreenDestination.Practice -> PracticeScreen(viewModel = viewModel)
                    ScreenDestination.Profile -> ProfileScreen(viewModel = viewModel)
                    ScreenDestination.RevisionRadar -> RevisionRadarScreen(viewModel = viewModel)
                    ScreenDestination.CameraSolver -> CameraSolverScreen(viewModel = viewModel)
                    ScreenDestination.VoiceTeacher -> VoiceTeacherScreen(viewModel = viewModel)
                    ScreenDestination.MindMap -> MindMapScreen(viewModel = viewModel)
                    ScreenDestination.Flashcards -> FlashcardScreen(viewModel = viewModel)
                    ScreenDestination.FocusMode -> FocusModeScreen(viewModel = viewModel)
                    ScreenDestination.AdminPanel -> AdminPanelScreen(viewModel = viewModel)
                    ScreenDestination.TestSeries -> TestSeriesScreen(viewModel = viewModel)
                    ScreenDestination.StudyPlan -> StudyPlanScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun RowScope.NavItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    isSpecial: Boolean = false
) {
    NavigationBarItem(
        selected = isSelected,
        onClick = onClick,
        icon = {
            if (isSpecial) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) NeonCyan else SpaceCardElevated)
                        .border(1.dp, NeonCyan, CircleShape),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = if (isSelected) SpaceDark else NeonCyan,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (isSelected) NeonCyan else SoftText
                )
            }
        },
        label = {
            Text(
                text = label,
                color = if (isSelected) NeonCyan else SoftText,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        },
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = if (isSpecial) Color.Transparent else SpaceCardElevated
        )
    )
}

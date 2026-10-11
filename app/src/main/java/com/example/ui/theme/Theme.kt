package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = SpaceDark,
    primaryContainer = SpaceCardElevated,
    onPrimaryContainer = NeonCyan,
    secondary = CyberViolet,
    onSecondary = Color.White,
    secondaryContainer = SpaceCard,
    onSecondaryContainer = CyberViolet,
    tertiary = HologramGold,
    onTertiary = SpaceDark,
    background = SpaceDark,
    onBackground = LightWhite,
    surface = SpaceCard,
    onSurface = LightWhite,
    surfaceVariant = SpaceCardElevated,
    onSurfaceVariant = SoftText,
    outline = SpaceCardBorder,
    error = AlertRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = LightAccent,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2FE),
    onPrimaryContainer = Color(0xFF0369A1),
    secondary = CyberPurple,
    onSecondary = Color.White,
    tertiary = FlameAmber,
    background = LightBackground,
    onBackground = LightPrimary,
    surface = LightSurface,
    onSurface = LightPrimary,
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = MutedText,
    outline = LightBorder,
    error = AlertRed,
    onError = Color.White
)

@Composable
fun AnkitStudyAiTheme(
    darkTheme: Boolean = true, // Default to futuristic dark aesthetic
    dynamicColor: Boolean = false, // Keep branded cyberpunk palette
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

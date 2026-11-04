package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val OnyxDarkColorScheme = darkColorScheme(
    primary = OnyxGold,
    onPrimary = OnyxBlack,
    primaryContainer = OnyxGoldDark,
    onPrimaryContainer = OnyxGoldLight,
    secondary = NeonCyan,
    onSecondary = OnyxBlack,
    secondaryContainer = OnyxSurfaceVariant,
    onSecondaryContainer = NeonCyan,
    tertiary = NeonEmerald,
    onTertiary = OnyxBlack,
    background = OnyxBlack,
    onBackground = TextWhite,
    surface = OnyxSurface,
    onSurface = TextWhite,
    surfaceVariant = OnyxSurfaceVariant,
    onSurfaceVariant = TextMuted,
    error = CrimsonAlert,
    onError = Color.White,
    outline = BorderDark
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Preserve strict luxury Onyx branding
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = OnyxDarkColorScheme,
        typography = Typography,
        content = content
    )
}

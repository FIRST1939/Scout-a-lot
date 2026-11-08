package com.frcteam1939.scouting2026.android.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Immutable
data class ScoutColors(
    val warningContainer: Color,
    val onWarningContainer: Color,
    val allianceRed: Color,
    val onAllianceRed: Color,
    val allianceBlue: Color,
    val onAllianceBlue: Color,
    val blueText: Color,
)

private val LightScoutColors = ScoutColors(
    warningContainer = Color(255, 227, 169),
    onWarningContainer = Color(77, 54, 0),
    allianceRed = Color(255, 218, 215),
    onAllianceRed = Color(112, 20, 16),
    allianceBlue = Color(214, 227, 255),
    onAllianceBlue = Color(14, 47, 102),
    blueText = Color(31, 91, 174),
)

private val DarkScoutColors = ScoutColors(
    warningContainer = Color(77, 54, 0),
    onWarningContainer = Color(255, 227, 169),
    allianceRed = Color(112, 20, 16),
    onAllianceRed = Color(255, 218, 215),
    allianceBlue = Color(14, 47, 102),
    onAllianceBlue = Color(214, 227, 255),
    blueText = Color(168, 199, 250),
)

private val LocalScoutColors = staticCompositionLocalOf { LightScoutColors }

val MaterialTheme.scoutColors: ScoutColors
    @Composable @ReadOnlyComposable
    get() = LocalScoutColors.current

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorScheme(
            primary = Color(134, 219, 115),
            onPrimary = Color(26, 80, 24),
            primaryContainer = Color(26, 80, 24),
            onPrimaryContainer = Color(196, 240, 181),
            background = Color(14, 19, 13),
            onBackground = Color(228, 233, 223),
            surface = Color(24, 29, 22),
            onSurface = Color(228, 233, 223),
            surfaceVariant = Color(40, 48, 37),
            onSurfaceVariant = Color(193, 200, 186),
            errorContainer = Color(112, 20, 16),
            onErrorContainer = Color(255, 218, 215),
            outline = Color(128, 135, 123),
            outlineVariant = Color(40, 48, 37)
        )
    } else {
        lightColorScheme(
            primary = Color(15, 109, 12),
            onPrimary = Color(255, 255, 255),
            primaryContainer = Color(186, 240, 181),
            onPrimaryContainer = Color(24, 29, 22),
            background = Color(245, 247, 242),
            onBackground = Color(24, 29, 22),
            surface = Color(255, 255, 255),
            onSurface = Color(24, 29, 22),
            surfaceVariant = Color(228, 233, 223),
            onSurfaceVariant = Color(65, 70, 61),
            errorContainer = Color(255, 218, 215),
            onErrorContainer = Color(112, 20, 16),
            outline = Color(128, 135, 123),
            outlineVariant = Color(228, 233, 223)
        )
    }
    val typography = Typography(
        bodyLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),
        bodySmall = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp
        )
    )
    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(8.dp),
        large = RoundedCornerShape(16.dp)
    )

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}

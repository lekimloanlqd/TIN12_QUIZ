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
    primary = QuizPurpleLight,
    onPrimary = Color.White,
    primaryContainer = QuizPurpleDark,
    onPrimaryContainer = Color.White,
    secondary = QuizOptionYellow,
    onSecondary = Color.Black,
    tertiary = QuizOptionGreen,
    background = QuizDarkBackground,
    surface = QuizDarkSurface,
    surfaceVariant = QuizDarkSurfaceVariant,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = QuizPurplePrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEDE9FE),
    onPrimaryContainer = QuizPurpleDark,
    secondary = QuizOptionYellow,
    onSecondary = Color.White,
    tertiary = QuizOptionGreen,
    background = QuizBackground,
    surface = QuizSurface,
    surfaceVariant = Color(0xFFF1F2F6),
    onBackground = Color(0xFF2D3436),
    onSurface = Color(0xFF2D3436),
    error = QuizError,
    onError = Color.White
)

@Composable
fun QuizSmartTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent Quizizz branding
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

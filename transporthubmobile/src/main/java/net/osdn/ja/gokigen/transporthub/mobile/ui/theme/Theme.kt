package net.osdn.ja.gokigen.transporthub.mobile.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(color = 0xFF018786), // AccentGreen,
    onPrimary = Color.White,
    primaryContainer = SubGreen,
    onPrimaryContainer = Color.Black,

    secondary = BaseGreen,
    onSecondary = Color.White,
    secondaryContainer = SubGreen,
    onSecondaryContainer = Color.Black,

    tertiary = SubGreen,
    onTertiary = Color.White,

    background = Color.Black,
    onBackground = Color.White,

    surface = Color.Black,
    onSurface = Color.White,

    // その他の色（error, onError, surfaceVariant, onSurfaceVariantなど）も必要に応じて追加
    error = Color(0xFFB00020),
    onError = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = Color(color = 0xFF018786), // BaseGreen,
    onPrimary = Color.White, // Color.Black,
    primaryContainer = AccentGreen,
    onPrimaryContainer = Color.White,

    secondary = SubGreen,
    onSecondary = Color.Black,
    secondaryContainer = AccentGreen,
    onSecondaryContainer = Color.Black,

    tertiary = AccentGreen,
    onTertiary = Color.Black,

    background = Color.White,
    onBackground = Color.Black, // 背景が白なのでテキストは黒

    surface = Color.White,
    onSurface = Color.Black,

    error = Color(0xFFB00020),
    onError = Color.White,
)

@Composable
fun TransportHubTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
)
{
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

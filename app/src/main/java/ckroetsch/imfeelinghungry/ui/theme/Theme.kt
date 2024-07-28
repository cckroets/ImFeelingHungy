package ckroetsch.imfeelinghungry.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val LightPrimary = Color(0xff1a1a1a)
val LightOnPrimary = Color.White
val LightSecondary = Color(0xff00bcd4)
val LightBackground = Color(0xffe0f7fa)
val LightSurface = Color.White
val LightOnSurface = Color.Black
val LightPunchyColor = Color(0xffffd32b)

val DarkPrimary = Color(0xff212121)
val DarkOnPrimary = Color.White
val DarkSecondary = Color(0xff03a9f4)
val DarkBackground = Color(0xff303030)
val DarkSurface = Color(0xff424242)
val DarkOnSurface = Color.White
val DarkPunchyColor = Color(0xffffd32b)

val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    secondary = LightSecondary,
    background = LightBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    primaryContainer = LightPunchyColor // Use the punchy yellow for highlights
)

val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    secondary = DarkSecondary,
    background = DarkBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    primaryContainer = DarkPunchyColor // Use the punchy yellow for highlights
)

@Composable
fun ImFeelingHungryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

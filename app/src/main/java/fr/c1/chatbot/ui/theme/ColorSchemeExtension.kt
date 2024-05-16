package fr.c1.chatbot.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
class ColorSchemeExtension (
    val bot: Color = Color.Unspecified,
    val user: Color = Color.Unspecified
)

val LightColorSchemeExtension = ColorSchemeExtension(
    bot = Color.Gray,
    user = Color.Blue
)

val DarkColorSchemeExtension = ColorSchemeExtension(
    bot = Color.Gray,
    user = Color.Blue
)

fun DynamicColorSchemeExtension(colorScheme: ColorScheme) = ColorSchemeExtension(
    bot = colorScheme.primary,
    user = colorScheme.secondary
)

val LocalColorSchemeExtension = staticCompositionLocalOf { ColorSchemeExtension() }
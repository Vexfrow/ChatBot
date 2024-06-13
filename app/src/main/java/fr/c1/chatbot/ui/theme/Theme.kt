package fr.c1.chatbot.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import android.os.Build
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Purple80, secondary = PurpleGrey80, tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40, secondary = PurpleGrey40, tertiary = Pink40
)


@Composable
fun ChatBotTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true, content: @Composable () -> Unit
) {
    val colorScheme: ColorScheme
    val colorSchemeExtension: ColorSchemeExtension

    when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            colorScheme = if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)

            colorSchemeExtension = DynamicColorSchemeExtension(colorScheme)
        }

        darkTheme -> {
            colorScheme = DarkColorScheme
            colorSchemeExtension = DarkColorSchemeExtension
        }

        else -> {
            colorScheme = LightColorScheme
            colorSchemeExtension = LightColorSchemeExtension
        }
    }

    CompositionLocalProvider(
        LocalColorSchemeExtension provides colorSchemeExtension
    ) {
        MaterialTheme(
            colorScheme = colorScheme, typography = Typography(), content = content
        )
    }
}

@Composable
fun ChatBotPrev(content: @Composable BoxScope.() -> Unit) = ChatBotTheme {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box { content() }
    }
}
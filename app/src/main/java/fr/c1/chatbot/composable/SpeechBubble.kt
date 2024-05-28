package fr.c1.chatbot.composable

import fr.c1.chatbot.ui.shape.SpeechBubbleShape
import fr.c1.chatbot.ui.theme.ChatBotPrev
import fr.c1.chatbot.ui.theme.LocalColorSchemeExtension
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import android.content.res.Configuration

@Composable
fun SpeechBubble(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = LocalColorSchemeExtension.current.bot,
    cornerRadius: Dp = 15.dp,
    tipSize: Dp = 15.dp,
    reversed: Boolean = false
) {
    var mod = modifier
        .size(400.dp, 200.dp)

    if (reversed)
        mod = mod.graphicsLayer(rotationY = 180f)

    mod = mod.clip(SpeechBubbleShape(cornerRadius, tipSize))

    if (reversed)
        mod = mod.graphicsLayer(rotationY = -180f)

    mod = mod.background(color)

    Box(modifier = mod) {
        val padding = if (reversed) PaddingValues(
            start = 5.dp,
            top = 5.dp,
            end = tipSize + 5.dp,
            bottom = tipSize + 5.dp
        ) else PaddingValues(
            start = tipSize + 5.dp,
            top = 5.dp,
            end = 5.dp,
            bottom = tipSize + 5.dp
        )

        Text(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = MaterialTheme.typography.bodyLarge.fontSize
        )
    }
}

@Preview(device = Devices.PIXEL_TABLET)
@Preview(device = Devices.PIXEL_TABLET, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Prev() = ChatBotPrev {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        SpeechBubble(
            modifier = Modifier.offset(10.dp, 10.dp),
            text = "Test ${(0..25).joinToString()}"
        )

        Text(
            modifier = Modifier
                .align(Alignment.TopEnd),
            text = "Test"
        )
    }
}
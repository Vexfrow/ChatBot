package fr.c1.chatbot.ui.shape

import fr.c1.chatbot.ui.theme.ChatBotPrev
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import android.content.res.Configuration

class SpeechBubbleShape(
    private val cornerRadius: Dp = 15.dp,
    private val tipSize: Dp = 15.dp
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val tipSize = with(density) { tipSize.toPx() }
        val cornerRadius = with(density) { cornerRadius.toPx() }
        val path = Path().apply {
            addRoundRect(
                RoundRect(
                    left = tipSize,
                    top = 0f,
                    right = size.width,
                    bottom = size.height - tipSize,
                    radiusX = cornerRadius,
                    radiusY = cornerRadius
                )
            )

            moveTo(
                x = tipSize,
                y = size.height - tipSize - cornerRadius
            )

            lineTo(
                x = 0f,
                y = size.height
            )

            lineTo(
                x = tipSize + cornerRadius,
                y = size.height - tipSize
            )

            close()
        }

        return Outline.Generic(path)
    }
}

@Preview(device = Devices.PIXEL_TABLET)
@Preview(
    device = Devices.PIXEL_TABLET, uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun Shape() {
    ChatBotPrev {
        Box(
            modifier = Modifier
                .size(400.dp, 200.dp)
                .clip(SpeechBubbleShape())
                .background(Color.Red)
        )
    }
}
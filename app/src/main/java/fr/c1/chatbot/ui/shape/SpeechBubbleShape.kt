package fr.c1.chatbot.ui.shape

import fr.c1.chatbot.ui.theme.ChatBotPrev
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
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
    private val tipSize: Dp = 15.dp,
    private var sizeBubble: Size = Size(400f, 200f)
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val tipSizePx: Float
        val cornerRadiusPx: Float

        with(density) {
            tipSizePx = tipSize.toPx()
            cornerRadiusPx = cornerRadius.toPx()
        }

        val path = Path().apply {
            val rect = Rect(
                left = tipSizePx,
                top = 0f,
                right = sizeBubble.width,
                bottom = sizeBubble.height - tipSizePx
            )

            moveTo(
                x = rect.left + cornerRadiusPx * 2,
                y = rect.bottom
            )

            lineTo(
                x = 0f,
                y = sizeBubble.height
            )

            lineTo(
                x = rect.left,
                y = rect.bottom - cornerRadiusPx * 2
            )

            arcTo(
                rect = rect.copy(
                    bottom = cornerRadiusPx * 2,
                    right = rect.left + cornerRadiusPx * 2,
                ),
                startAngleDegrees = -180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            arcTo(
                rect = rect.copy(
                    bottom = cornerRadiusPx * 2,
                    left = rect.right - cornerRadiusPx * 2,
                ),
                startAngleDegrees = -90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            arcTo(
                rect = rect.copy(
                    left = rect.right - cornerRadiusPx * 2,
                    top = rect.bottom - cornerRadiusPx * 2,
                ),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
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
                .border(1.dp, Color.Green, SpeechBubbleShape())
        )
    }
}

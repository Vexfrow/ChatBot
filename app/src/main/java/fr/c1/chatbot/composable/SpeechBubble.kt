package fr.c1.chatbot.composable

import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import fr.c1.chatbot.model.Settings
import fr.c1.chatbot.ui.shape.SpeechBubbleShape
import fr.c1.chatbot.ui.theme.ChatBotPrev
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import android.net.Uri

@Composable
fun SpeechBubble(
    text: String,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 15.dp,
    tipSize: Dp = 15.dp,
    isUser: Boolean = false
) {

    var textSize by remember { mutableStateOf(IntSize(0, 0)) }
    var realWidth by remember { mutableIntStateOf(0) }
    val minimumWidth = 600

    var mod = modifier

    if (isUser)
        mod = mod.graphicsLayer(rotationY = 180f)

    val shapeB = SpeechBubbleShape(cornerRadius, tipSize, textSize.toSize())

    mod = mod.clip(shapeB).border(width = 4.dp, color = Color.Black, shape = shapeB)

    if (isUser)
        mod = mod.graphicsLayer(rotationY = -180f)

    val color = if (isUser) Settings.bubbleSpeechUserColor else Settings.bubbleSpeechBotColor
    mod = mod.background(color)

    Box(modifier = mod.fillMaxWidth()) {
        val padding = if (isUser) PaddingValues(
            start = 20.dp,
            top = 10.dp,
            end = tipSize + 10.dp,
            bottom = tipSize + 50.dp
        ) else PaddingValues(
            start = tipSize + 20.dp,
            top = 10.dp,
            end = 30.dp,
            bottom = tipSize + 45.dp
        )

        Text(
            modifier = mod
                .fillMaxWidth()
                .padding(padding)
                .onGloballyPositioned { layoutCoordinates ->
                    realWidth =
                        (layoutCoordinates.size.width + tipSize.value.toInt() + padding.calculateEndPadding(
                            LayoutDirection.Ltr
                        ).value + padding.calculateStartPadding(LayoutDirection.Ltr).value).toInt()
                    val heightT =
                        layoutCoordinates.size.height + padding.calculateTopPadding().value.toInt() + padding.calculateBottomPadding().value.toInt()

                    textSize = if (realWidth < minimumWidth) IntSize(minimumWidth, heightT)
                    else IntSize(realWidth, heightT)
                },
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = MaterialTheme.typography.bodyLarge.fontSize,
            color = if (isUser) Settings.textUserColor else Settings.textBotColor
        )
    }
}


@Composable
fun MessageComponent(
    text: String,
    modifier: Modifier = Modifier,
    isUser: Boolean = false
) {
    Box(
        modifier = modifier
            .widthIn(600.dp, 600.dp) // Adjusted width
            .heightIn(0.dp, 600.dp) // Adjusted height
            .padding(10.dp) // General padding for the box
    ) {
        // Speech Bubble positioned in the upper right corner
        Column(
            modifier = if (isUser)
                Modifier
                    .align(Alignment.TopStart)
                    .padding(end = 120.dp)
            else Modifier
                .align(Alignment.TopEnd)
                .padding(start = 120.dp)// Adjust padding to position bubble correctly
        ) {
            SpeechBubble(
                text = text,
                isUser = isUser
            )
        }

        // Icon or Image positioned in the lower left corner
        val iconModifier = if (isUser)
            Modifier
                .size(100.dp)
                .align(Alignment.BottomEnd) // Aligning to the bottom start (lower left corner)
                .padding(PaddingValues(end = 20.dp, top = 10.dp))
        else
            Modifier
                .size(100.dp)
                .align(Alignment.BottomStart) // Aligning to the bottom start (lower left corner)
                .padding(PaddingValues(start = 20.dp, top = 10.dp))

        val context = LocalContext.current

        @Composable
        fun MyIcon(icon: ImageVector) {
            Icon(
                modifier = iconModifier,
                imageVector = icon,
                contentDescription = null
            )
        }

        @Composable
        fun MyImage(img: Uri) {
            Image(
                modifier = iconModifier,
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context).data(img).build()
                ),
                contentDescription = null
            )
        }

        if (!isUser) {
            if (Settings.botImage == null) {
                MyIcon(icon = Settings.botIcon)
            } else {
                MyImage(img = Settings.botImage!!)
            }
        } else {
            if (Settings.userImage == null) {
                MyIcon(icon = Settings.userIcon)
            } else {
                MyImage(img = Settings.userImage!!)
            }
        }
    }
}


@Preview(device = Devices.PIXEL_TABLET)
@Composable
private fun Prev() = ChatBotPrev {
    MessageComponent(
        "Oui",
        Modifier.align(Alignment.TopStart), isUser = true
    )
}
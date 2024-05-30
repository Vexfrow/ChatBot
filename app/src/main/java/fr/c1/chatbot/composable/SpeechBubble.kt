package fr.c1.chatbot.composable

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import fr.c1.chatbot.model.Settings
import fr.c1.chatbot.ui.shape.SpeechBubbleShape
import fr.c1.chatbot.ui.theme.ChatBotPrev
import fr.c1.chatbot.ui.theme.LocalColorSchemeExtension

@Composable
fun SpeechBubble(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = LocalColorSchemeExtension.current.bot,
    cornerRadius: Dp = 15.dp,
    tipSize: Dp = 15.dp,
    isUser: Boolean = false
) {

    var textSize by remember { mutableStateOf(IntSize(0, 0)) }
    var text2 by remember { mutableStateOf(text) }

    text2 = text

    var mod = modifier

    if (isUser)
        mod = mod.graphicsLayer(rotationY = 180f)

    mod = mod.clip(SpeechBubbleShape(cornerRadius, tipSize, textSize.toSize()))

    if (isUser)
        mod = mod.graphicsLayer(rotationY = -180f)

    mod = mod.background(color)

    Box(modifier = mod.widthIn(0.dp, 500.dp)) {
        val padding = if (isUser) PaddingValues(
            start = 50.dp,
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
                .padding(padding)
                .onGloballyPositioned { layoutCoordinates ->
                    textSize = IntSize(
                        (layoutCoordinates.size.width + tipSize.value.toInt() + padding.calculateEndPadding(
                            LayoutDirection.Ltr
                        ).value + padding.calculateStartPadding(LayoutDirection.Ltr).value).toInt(),
                        layoutCoordinates.size.height + padding.calculateTopPadding().value.toInt() + padding.calculateBottomPadding().value.toInt()
                    )
                },
            text = text2,
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = MaterialTheme.typography.bodyLarge.fontSize
        )
    }
}


@Composable
fun Message(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = LocalColorSchemeExtension.current.bot,
    isUser: Boolean = false
) {
    //var textSize by remember { mutableStateOf(IntSize(100, 100)) }

    Box(
        modifier = modifier
            .widthIn(0.dp, 600.dp) // Adjusted width
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
                color = color,
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
//@Preview(device = Devices.PIXEL_TABLET, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Prev() = ChatBotPrev {
    Message(
        " J'ai envie de another test bite the dust",
        Modifier.align(Alignment.TopStart), isUser = false
    )
    //SpeechBubble(text = "bllllllllllllllllllllllbblblblblblblblblblblblblblblbllblblblb",modifier = Modifier.align(Alignment.TopStart),isUser = true)
}
package fr.c1.chatbot.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
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
            start = 20.dp,
            top = 10.dp,
            end = tipSize + 5.dp,
            bottom = tipSize + 50.dp
        ) else PaddingValues(
            start = tipSize + 10.dp,
            top = 5.dp,
            end = 5.dp,
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
private fun CircleBot(
    color: Color,
    radius: Float,
    modifier: Modifier = Modifier
) {

}


@Composable
fun MessageBot(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = LocalColorSchemeExtension.current.bot,
    isUser: Boolean = false

) {
    var textSize by remember { mutableStateOf(IntSize(100, 100)) }

    Box(
        modifier = modifier
            .widthIn(0.dp, 500.dp + 150f.dp)
            .heightIn(0.dp, 500.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(PaddingValues(start = 150f.dp))
                .onGloballyPositioned { layoutCoordinates ->
                    textSize = layoutCoordinates.size
                },
            verticalArrangement = Arrangement.spacedBy(100.dp) // Adjust the spacing as needed
        ) {
            SpeechBubble(
                text = text,
                isUser = isUser
            )
        }

        CircleBot(
            color = Color.Red,
            radius = 80f,
            modifier = Modifier
                //.align(Alignment.BottomStart)
                .padding(PaddingValues(top = textSize.height.dp))
        )
    }

}


@Preview(device = Devices.PIXEL_TABLET)
//@Preview(device = Devices.PIXEL_TABLET, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Prev() = ChatBotPrev {
    MessageBot(" Jtsttttt, je n'aime pas le cassoulet", Modifier.align(Alignment.TopStart))
    //SpeechBubble(text = "bllllllllllllllllllllllbblblblblblblblblblblblblblblbllblblblb",modifier = Modifier.align(Alignment.TopStart),isUser = true)
}
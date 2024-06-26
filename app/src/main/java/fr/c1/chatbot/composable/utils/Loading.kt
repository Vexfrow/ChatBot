package fr.c1.chatbot.composable.utils

import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import fr.c1.chatbot.model.Settings
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun Loading(message: String) = Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    CircularProgressIndicator(modifier = Modifier.size(96.dp))
    MyText(
        text = message,
        textAlign = TextAlign.Center
    )
}

private const val rotationRange = 45f
private const val rotationDuration = 500

@Composable
fun BotLoading() = Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
) {
    val ir = rememberInfiniteTransition()
    val rot by ir.animateFloat(
        initialValue = -rotationRange,
        targetValue = rotationRange,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = rotationDuration * 4
                0f at 0 using LinearEasing
                rotationRange at rotationDuration using LinearEasing
                0f at rotationDuration * 2 using LinearEasing
                (-rotationRange) at rotationDuration * 3 using LinearEasing
                0f at rotationDuration * 4 using LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    CircularProgressIndicator(
        modifier = Modifier.size(96.dp)
    )

    if (Settings.botImage == null) Icon(
        modifier = Modifier
            .size(48.dp)
            .graphicsLayer(rotationZ = rot),
        imageVector = Settings.botIcon,
        contentDescription = "Robot loading"
    )
    else Image(
        modifier = Modifier
            .size(48.dp)
            .graphicsLayer(rotationZ = rot),
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current).data(Settings.botImage).build()
        ),
        contentDescription = "Robot loading"
    )
}
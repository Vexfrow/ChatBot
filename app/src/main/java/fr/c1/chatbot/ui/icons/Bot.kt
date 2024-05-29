package fr.c1.chatbot.ui.icons

import fr.c1.chatbot.ui.theme.ChatBotPrev
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val Icons.Filled.Bot: ImageVector
    get() {
        if (_bot != null)
            return _bot!!

        _bot = materialIcon("Filled.Bot") {
            materialPath {
                moveTo(17.753f, 14.0f)
                curveTo(18.996f, 14.0f, 20.003f, 15.007f, 20.003f, 16.25f)
                lineTo(20.003f, 17.155f)
                curveTo(20.003f, 18.249f, 19.526f, 19.288f, 18.696f, 20.0f)
                curveTo(17.13f, 21.344f, 14.89f, 22.001f, 12.0f, 22.001f)
                curveTo(9.111f, 22.001f, 6.872f, 21.344f, 5.309f, 20.001f)
                curveTo(4.48f, 19.288f, 4.004f, 18.25f, 4.004f, 17.157f)
                lineTo(4.004f, 16.25f)
                curveTo(4.004f, 15.007f, 5.011f, 14.0f, 6.254f, 14.0f)
                lineTo(17.753f, 14.0f)
                close()
                moveTo(17.753f, 15.5f)
                lineTo(6.254f, 15.5f)
                curveTo(5.839f, 15.5f, 5.504f, 15.836f, 5.504f, 16.25f)
                lineTo(5.504f, 17.157f)
                curveTo(5.504f, 17.813f, 5.79f, 18.436f, 6.287f, 18.863f)
                curveTo(7.545f, 19.945f, 9.441f, 20.501f, 12.0f, 20.501f)
                curveTo(14.56f, 20.501f, 16.458f, 19.945f, 17.719f, 18.862f)
                curveTo(18.217f, 18.435f, 18.503f, 17.811f, 18.503f, 17.155f)
                lineTo(18.503f, 16.25f)
                curveTo(18.503f, 15.836f, 18.167f, 15.5f, 17.753f, 15.5f)
                close()
                moveTo(11.899f, 2.007f)
                lineTo(12.0f, 2.0f)
                curveTo(12.38f, 2.0f, 12.694f, 2.283f, 12.743f, 2.649f)
                lineTo(12.75f, 2.75f)
                lineTo(12.75f, 3.499f)
                lineTo(16.25f, 3.5f)
                curveTo(17.493f, 3.5f, 18.5f, 4.507f, 18.5f, 5.75f)
                lineTo(18.5f, 10.255f)
                curveTo(18.5f, 11.497f, 17.493f, 12.505f, 16.25f, 12.505f)
                lineTo(7.75f, 12.505f)
                curveTo(6.507f, 12.505f, 5.5f, 11.497f, 5.5f, 10.255f)
                lineTo(5.5f, 5.75f)
                curveTo(5.5f, 4.507f, 6.507f, 3.5f, 7.75f, 3.5f)
                lineTo(11.25f, 3.499f)
                lineTo(11.25f, 2.75f)
                curveTo(11.25f, 2.371f, 11.532f, 2.057f, 11.899f, 2.007f)
                lineTo(12.0f, 2.0f)
                lineTo(11.899f, 2.007f)
                close()
                moveTo(16.25f, 5.0f)
                lineTo(7.75f, 5.0f)
                curveTo(7.336f, 5.0f, 7.0f, 5.336f, 7.0f, 5.75f)
                lineTo(7.0f, 10.255f)
                curveTo(7.0f, 10.669f, 7.336f, 11.005f, 7.75f, 11.005f)
                lineTo(16.25f, 11.005f)
                curveTo(16.664f, 11.005f, 17.0f, 10.669f, 17.0f, 10.255f)
                lineTo(17.0f, 5.75f)
                curveTo(17.0f, 5.336f, 16.664f, 5.0f, 16.25f, 5.0f)
                close()
                moveTo(9.749f, 6.5f)
                curveTo(10.439f, 6.5f, 10.999f, 7.059f, 10.999f, 7.749f)
                curveTo(10.999f, 8.439f, 10.439f, 8.999f, 9.749f, 8.999f)
                curveTo(9.059f, 8.999f, 8.5f, 8.439f, 8.5f, 7.749f)
                curveTo(8.5f, 7.059f, 9.059f, 6.5f, 9.749f, 6.5f)
                close()
                moveTo(14.242f, 6.5f)
                curveTo(14.932f, 6.5f, 15.491f, 7.059f, 15.491f, 7.749f)
                curveTo(15.491f, 8.439f, 14.932f, 8.999f, 14.242f, 8.999f)
                curveTo(13.552f, 8.999f, 12.993f, 8.439f, 12.993f, 7.749f)
                curveTo(12.993f, 7.059f, 13.552f, 6.5f, 14.242f, 6.5f)
                close()
            }
        }

        return _bot!!
    }

private var _bot: ImageVector? = null

@Preview(device = Devices.TABLET)
@Composable
private fun Prev() = ChatBotPrev {
    Icon(modifier = Modifier.size(100.dp), imageVector = Icons.Default.Bot, contentDescription = "")
}
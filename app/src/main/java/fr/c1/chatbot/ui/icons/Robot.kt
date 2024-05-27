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

val Icons.Filled.Robot: ImageVector
    get() {
        if (_robot != null)
            return _robot!!

        _robot = materialIcon("Filled.Robot") {
            materialPath {
                moveTo(14.0f, 2.0f)
                curveTo(14.0f, 2.74f, 13.598f, 3.387f, 13.0f, 3.732f)
                verticalLineTo(4.0f)
                horizontalLineTo(20.0f)
                curveTo(21.657f, 4.0f, 23.0f, 5.343f, 23.0f, 7.0f)
                verticalLineTo(19.0f)
                curveTo(23.0f, 20.657f, 21.657f, 22.0f, 20.0f, 22.0f)
                horizontalLineTo(4.0f)
                curveTo(2.343f, 22.0f, 1.0f, 20.657f, 1.0f, 19.0f)
                verticalLineTo(7.0f)
                curveTo(1.0f, 5.343f, 2.343f, 4.0f, 4.0f, 4.0f)
                horizontalLineTo(11.0f)
                verticalLineTo(3.732f)
                curveTo(10.402f, 3.387f, 10.0f, 2.74f, 10.0f, 2.0f)
                curveTo(10.0f, 0.895f, 10.895f, 0.0f, 12.0f, 0.0f)
                curveTo(13.105f, 0.0f, 14.0f, 0.895f, 14.0f, 2.0f)
                close()
                moveTo(4.0f, 6.0f)
                curveTo(3.448f, 6.0f, 3.0f, 6.448f, 3.0f, 7.0f)
                verticalLineTo(19.0f)
                curveTo(3.0f, 19.552f, 3.448f, 20.0f, 4.0f, 20.0f)
                horizontalLineTo(20.0f)
                curveTo(20.552f, 20.0f, 21.0f, 19.552f, 21.0f, 19.0f)
                verticalLineTo(7.0f)
                curveTo(21.0f, 6.448f, 20.552f, 6.0f, 20.0f, 6.0f)
                horizontalLineTo(13.0f)
                horizontalLineTo(11.0f)
                horizontalLineTo(4.0f)
                close()
                moveTo(15.0f, 11.5f)
                curveTo(15.0f, 10.672f, 15.672f, 10.0f, 16.5f, 10.0f)
                curveTo(17.328f, 10.0f, 18.0f, 10.672f, 18.0f, 11.5f)
                curveTo(18.0f, 12.328f, 17.328f, 13.0f, 16.5f, 13.0f)
                curveTo(15.672f, 13.0f, 15.0f, 12.328f, 15.0f, 11.5f)
                close()
                moveTo(16.5f, 8.0f)
                curveTo(14.567f, 8.0f, 13.0f, 9.567f, 13.0f, 11.5f)
                curveTo(13.0f, 13.433f, 14.567f, 15.0f, 16.5f, 15.0f)
                curveTo(18.433f, 15.0f, 20.0f, 13.433f, 20.0f, 11.5f)
                curveTo(20.0f, 9.567f, 18.433f, 8.0f, 16.5f, 8.0f)
                close()
                moveTo(7.5f, 10.0f)
                curveTo(6.672f, 10.0f, 6.0f, 10.672f, 6.0f, 11.5f)
                curveTo(6.0f, 12.328f, 6.672f, 13.0f, 7.5f, 13.0f)
                curveTo(8.328f, 13.0f, 9.0f, 12.328f, 9.0f, 11.5f)
                curveTo(9.0f, 10.672f, 8.328f, 10.0f, 7.5f, 10.0f)
                close()
                moveTo(4.0f, 11.5f)
                curveTo(4.0f, 9.567f, 5.567f, 8.0f, 7.5f, 8.0f)
                curveTo(9.433f, 8.0f, 11.0f, 9.567f, 11.0f, 11.5f)
                curveTo(11.0f, 13.433f, 9.433f, 15.0f, 7.5f, 15.0f)
                curveTo(5.567f, 15.0f, 4.0f, 13.433f, 4.0f, 11.5f)
                close()
                moveTo(10.894f, 16.553f)
                curveTo(10.647f, 16.059f, 10.047f, 15.859f, 9.553f, 16.106f)
                curveTo(9.059f, 16.353f, 8.859f, 16.953f, 9.106f, 17.447f)
                curveTo(9.681f, 18.597f, 10.982f, 19.0f, 12.0f, 19.0f)
                curveTo(13.018f, 19.0f, 14.319f, 18.597f, 14.894f, 17.447f)
                curveTo(15.141f, 16.953f, 14.941f, 16.353f, 14.447f, 16.106f)
                curveTo(13.953f, 15.859f, 13.353f, 16.059f, 13.106f, 16.553f)
                curveTo(13.014f, 16.736f, 12.649f, 17.0f, 12.0f, 17.0f)
                curveTo(11.351f, 17.0f, 10.986f, 16.736f, 10.894f, 16.553f)
                close()
            }
        }

        return _robot!!
    }

private var _robot: ImageVector? = null

@Preview(device = Devices.TABLET)
@Composable
private fun Prev() = ChatBotPrev {
    Icon(
        modifier = Modifier.size(100.dp),
        imageVector = Icons.Default.Robot,
        contentDescription = ""
    )
}
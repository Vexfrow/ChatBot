package fr.c1.chatbot.ui.icons

import fr.c1.chatbot.R
import fr.c1.chatbot.ui.theme.ChatBotPrev
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val Icons.Filled.Deceased: ImageVector
    @Composable
    get() {
        if (_deceased != null)
            return _deceased!!

        _deceased = ImageVector.vectorResource(id = R.drawable.deceased)

        return _deceased!!
    }

private var _deceased: ImageVector? = null

@Preview(device = Devices.TABLET)
@Composable
private fun Prev() = ChatBotPrev {
    Icon(
        modifier = Modifier.size(100.dp),
        imageVector = Icons.Default.Deceased,
        contentDescription = ""
    )
}
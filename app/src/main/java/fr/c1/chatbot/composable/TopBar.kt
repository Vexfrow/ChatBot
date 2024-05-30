package fr.c1.chatbot.composable

import fr.c1.chatbot.ui.icons.Robot
import fr.c1.chatbot.ui.theme.ChatBotPrev
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import android.util.Log

private val tabs = listOf(
    "Chatbot" to Icons.Default.Robot,
    "Résultats" to Icons.Default.Search
)

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    selectedIndex: Int = 0,
    onTabSelected: (Int) -> Unit
) {
    var state by remember { mutableIntStateOf(selectedIndex) }

    TabRow(selectedTabIndex = state, modifier = modifier) {
        tabs.forEachIndexed { i, (title, icon) ->
            Tab(
                selected = i == state,
                onClick = {
                    state = i
                    onTabSelected(i)
                },
                text = { Text(text = title) },
                icon = { Icon(imageVector = icon, contentDescription = title) }
            )
        }
    }
}

private const val TAG = "TopBar"

@Preview(device = Devices.TABLET)
@Composable
private fun Prev() = ChatBotPrev {
    TopBar {
        Log.i(TAG, "Prev: $it")
    }
}
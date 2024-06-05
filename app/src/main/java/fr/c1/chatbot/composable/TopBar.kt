package fr.c1.chatbot.composable

import fr.c1.chatbot.ui.icons.Robot
import fr.c1.chatbot.ui.theme.ChatBotPrev
import fr.c1.chatbot.utils.rememberMutableStateOf
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPasteSearch
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import android.util.Log

private val tabs = listOf(
    "Chatbot" to Icons.Default.Robot,
    "Suggestion" to Icons.Default.Lightbulb,
    "Historique" to Icons.Default.History,
    "Paramètres" to Icons.Default.Settings
)

private val chatBotSubTabs = listOf(
    "Conversation" to Icons.Default.Forum,
    "Resultats" to Icons.Default.ContentPasteSearch
)

enum class Tab(val value: Float) {
    ChatBotChat(0.0f),
    ChatBotResults(0.1f),
    Suggestion(1f),
    History(2f),
    Settings(3f);

    companion object {
        fun valueOf(value: Float) = entries.first { it.value == value }
    }
}

@Composable
fun TopBar(
    tabSelected: Float,
    onTabSelected: (Float) -> Unit
) {
    var state by remember { mutableFloatStateOf(tabSelected) }
    val subState by remember { derivedStateOf { ((state * 10) % 10).toInt() } }

    fun setState(value: Float) {
        state = value
        onTabSelected(state)
    }

    Column(modifier = Modifier.statusBarsPadding()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TabRow(selectedTabIndex = state.toInt()) {
                tabs.forEachIndexed { i, (title, icon) ->
                    Tab(
                        selected = i == state.toInt(),
                        onClick = { setState(i.toFloat()) },
                        text = { Text(text = title) },
                        icon = { Icon(imageVector = icon, contentDescription = title) }
                    )
                }
            }
        }

        if (state.toInt() == 0)
            TabRow(selectedTabIndex = subState) {
                chatBotSubTabs.forEachIndexed { i, (title, icon) ->
                    Tab(
                        selected = i == ((state * 10) % 10).toInt(),
                        onClick = { setState(i / 10f) },
                        text = { Text(text = title) },
                        icon = { Icon(imageVector = icon, contentDescription = title) }
                    )
                }
            }
    }
}

private const val TAG = "TopBar"

@Preview(device = Devices.TABLET)
@Composable
private fun Prev() = ChatBotPrev {
    var set by rememberMutableStateOf(value = false)
    TopBar(0f) { Log.i(TAG, "Prev: $it") }
}
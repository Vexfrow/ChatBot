package fr.c1.chatbot.composable

import fr.c1.chatbot.ui.icons.Robot
import fr.c1.chatbot.ui.theme.ChatBotPrev
import fr.c1.chatbot.utils.rememberMutableStateOf
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ContentPasteSearch
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.SentimentVerySatisfied
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import android.util.Log
import kotlin.math.floor

private val tabs = listOf(
    "Chatbot" to Icons.Default.Robot,
    "Suggestion" to Icons.Default.Lightbulb,
    "Historique" to Icons.Default.History,
    "Compte" to Icons.Default.AccountCircle,
    "Paramètres" to Icons.Default.Settings
)

private val chatBotSubTabs = listOf(
    "Conversation" to Icons.Default.Forum,
    "Resultats" to Icons.Default.ContentPasteSearch,
    "Carte" to Icons.Default.Map
)

private val accountSubTabs = listOf(
    "Données" to Icons.Default.AccountCircle,
    "Préférences hebdomadaire" to Icons.Default.DateRange,
    "Passions" to Icons.Default.SentimentVerySatisfied
)

enum class Tab(val value: Float) {
    ChatBotChat(0.0f),
    ChatBotResults(0.1f),
    ChatBotMap(0.2f),
    Suggestion(1f),
    History(2f),
    AccountData(3.0f),
    AccountPreferences(3.1f),
    AccountPassions(3.2f),
    Settings(4f);

    companion object {
        fun valueOf(value: Float) = entries.first { it.value == value }
    }
}

@Composable
fun TopBar(
    tabSelected: Float,
    onTabSelected: (Float) -> Unit
) {
    var state by remember(tabSelected) { mutableFloatStateOf(tabSelected) }
    val subState by remember(tabSelected) { derivedStateOf { ((state * 10) % 10).toInt() } }

    fun setState(value: Float) {
        state = value
        onTabSelected(state)
    }

    Column(modifier = Modifier.statusBarsPadding()) {
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

        val subTabs = when (floor(state)) {
            Tab.ChatBotChat.value -> chatBotSubTabs
            Tab.AccountData.value -> accountSubTabs
            else -> null
        }

        if (subTabs != null)
            TabRow(selectedTabIndex = subState) {
                subTabs.forEachIndexed { i, (title, icon) ->
                    Tab(
                        selected = i == ((state * 10) % 10).toInt(),
                        onClick = { setState(state.toInt() + i / 10f) },
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
package fr.c1.chatbot.composable

import fr.c1.chatbot.ui.icons.Robot
import fr.c1.chatbot.ui.theme.ChatBotPrev
import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import android.util.Log

/**
 * Enumeration representing all the tabs of the application
 *
 * @property value Value of the tab
 * @param value Value of the tab. Can be an [Int] or a [Float]
 * - [Int]: Tab [value]
 * - [Float]: Tab [value] with [Float.toInt] and subtab ([value] * 10) % 10
 * @property title Title of the tab
 * @property icon Icon of the tab
 *
 * @constructor Create a Tab with all his property
 */
enum class Tab(
    val value: Number,
    val title: String,
    val icon: ImageVector
) {
    ChatBot(0, "Chatbot", Icons.Default.Robot),
    ChatBotChat(0.0f, "Conversation", Icons.Default.Forum),
    ChatBotResults(0.1f, "Résultats", Icons.Default.ContentPasteSearch),
    ChatBotMap(0.2f, "Carte", Icons.Default.Map),
    Suggestion(1, "Suggestions", Icons.Default.Lightbulb),
    History(2, "Historique", Icons.Default.History),
    Account(3, "Compte", Icons.Default.AccountCircle),
    AccountData(3.0f, "Informations", Icons.Default.AccountCircle),
    AccountPreferences(3.1f, "Préférences hebdomadaires", Icons.Default.DateRange),
    AccountPassions(3.2f, "Centres d'intérêts", Icons.Default.SentimentVerySatisfied),
    Settings(4, "Paramètres", Icons.Default.Settings);

    /** Get a list of all sub tabs of the current tab */
    val subTabs: List<Tab> get() = entries.filter { it.value !is Int && it.value.toInt() == value.toInt() }
    /** Get the sub value of the current subtab */
    val subValue: Int get() = ((value.toFloat() * 10) % 10).toInt()
    /** Get the final tab or subtab of the current one */
    val finalTab: Tab get() = if (value is Float) this else subTabs.firstOrNull() ?: this
}

/**
 * Top bar of the application
 *
 * @param tabSelected Current tab selected
 * @param onTabSelected Callback when a tab is selected
 * @receiver
 */
@Composable
fun TopBar(
    tabSelected: Tab,
    onTabSelected: (Tab) -> Unit
) {
    var state by remember(tabSelected) { mutableStateOf(tabSelected) }

    fun setState(value: Tab) {
        state = value.finalTab
        onTabSelected(state)
    }

    Column(modifier = Modifier
        .statusBarsPadding()
        .background(Color.White)) {
        TabRow(selectedTabIndex = state.value.toInt()) {
            Tab.entries.filter { it.value is Int }.forEach { tab ->
                Tab(
                    selected = tab == state,
                    onClick = { setState(tab) },
                    text = { Text(text = tab.title) },
                    icon = { Icon(imageVector = tab.icon, contentDescription = tab.title) }
                )
            }
        }

        val subTabs = state.subTabs
        if (subTabs.isNotEmpty())
            TabRow(selectedTabIndex = state.subValue) {
                subTabs.forEach { subTab ->
                    Tab(
                        selected = subTab.value.toFloat() == state.value.toFloat(),
                        onClick = { setState(subTab) },
                        text = { Text(text = subTab.title) },
                        icon = {
                            Icon(
                                imageVector = subTab.icon,
                                contentDescription = subTab.title
                            )
                        }
                    )
                }
            }
    }
}

private const val TAG = "TopBar"

@Preview(device = Devices.TABLET)
@Composable
private fun Prev() = ChatBotPrev {
    TopBar(Tab.ChatBot) { Log.i(TAG, "Prev: $it") }
}
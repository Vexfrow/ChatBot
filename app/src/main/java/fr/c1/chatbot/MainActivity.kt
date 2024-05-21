package fr.c1.chatbot

import fr.c1.chatbot.composable.MySearchBar
import fr.c1.chatbot.composable.ProposalList
import fr.c1.chatbot.composable.SearchBar
import fr.c1.chatbot.composable.SpeechBubble
import fr.c1.chatbot.ui.theme.ChatBotPrev
import fr.c1.chatbot.ui.theme.ChatBotTheme
import fr.c1.chatbot.utils.rememberMutableStateListOf
import fr.c1.chatbot.utils.rememberMutableStateOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.os.Bundle
import android.util.Log

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatBotTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        val tree = (application as ChatBot).chatbotTree
                        val messages = rememberMutableStateListOf(tree.currentAnswer.question)

                        LazyColumn(
                            modifier = Modifier.weight(1f)
                        ) {
                            items(messages) { message ->
                                SpeechBubble(text = message)
                            }
                        }

                        var answers by rememberMutableStateOf(value = tree.getAnswersLabels())

                        ProposalList(proposals = answers) {
                            Log.i(TAG, "Choose '$it'")
                            val i = tree.currentAnswer.answers
                                .indexOfFirst { answer -> answer.label == it }
                            messages += it
                            tree.selectAnswer(i)
                            messages += tree.currentAnswer.question
                            answers = tree.getAnswersLabels()
                        }

                        var searchBarEnabled by rememberMutableStateOf(value = true)
                        var searchBarText by rememberMutableStateOf(value = "Search")

                        MySearchBar(
                            placeholder = searchBarText,
                            enabled = searchBarEnabled
                        ) { Log.i(TAG, "Searched $it") }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ChatBotTheme {
        Greeting("Android")
    }
}
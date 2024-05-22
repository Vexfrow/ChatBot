package fr.c1.chatbot

import fr.c1.chatbot.composable.MySearchBar
import fr.c1.chatbot.composable.ProposalList
import fr.c1.chatbot.composable.SpeechBubble
import fr.c1.chatbot.ui.theme.ChatBotTheme
import fr.c1.chatbot.ui.theme.colorSchemeExtension
import fr.c1.chatbot.utils.rememberMutableStateListOf
import fr.c1.chatbot.utils.rememberMutableStateOf
import kotlinx.coroutines.launch
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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

                        val crtScope = rememberCoroutineScope()
                        val lazyListState = rememberLazyListState()

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            state = lazyListState
                        ) {
                            itemsIndexed(messages) { i, message ->
                                val isBot = i % 2 == 0

                                if (isBot)
                                    SpeechBubble(
                                        text = message,
                                        color = MaterialTheme.colorSchemeExtension.bot,
                                    )
                                else
                                    Box(modifier = Modifier.fillMaxWidth()) {
                                        SpeechBubble(
                                            modifier = Modifier.align(Alignment.CenterEnd),
                                            text = message,
                                            color = MaterialTheme.colorSchemeExtension.user,
                                            reversed = true
                                        )
                                    }
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

                            crtScope.launch {
                                lazyListState.animateScrollToItem(lazyListState.layoutInfo.totalItemsCount)
                            }
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
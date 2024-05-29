package fr.c1.chatbot

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import fr.c1.chatbot.composable.Message
import fr.c1.chatbot.composable.MySearchBar
import fr.c1.chatbot.composable.MySettings
import fr.c1.chatbot.composable.ProposalList
import fr.c1.chatbot.model.Settings
import fr.c1.chatbot.ui.theme.ChatBotTheme
import fr.c1.chatbot.ui.theme.colorSchemeExtension
import fr.c1.chatbot.utils.application
import fr.c1.chatbot.utils.rememberMutableStateListOf
import fr.c1.chatbot.utils.rememberMutableStateOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatBotTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        var settings by rememberMutableStateOf(value = false)

                        MyColumn(modifier = Modifier, enabled = !settings)

                        val ctx = LocalContext.current
                        IconButton(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(end = 10.dp, top = 10.dp),
                            onClick = {
                                if (settings)
                                    Settings.save(ctx)

                                settings = !settings
                            }
                        ) {
                            Icon(
                                modifier = Modifier.size(50.dp),
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings"
                            )
                        }

                        if (settings)
                            MySettings()
                    }
                }
            }
        }
    }
}

@Composable
fun MyColumn(modifier: Modifier = Modifier, enabled: Boolean) {
    val tree = application.chatbotTree
    val messages = rememberMutableStateListOf(tree.getQuestion())
    val crtScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val animated = rememberMutableStateListOf<Boolean>()

    val tts = application.tts

    if (!enabled)
        return

    LaunchedEffect(key1 = messages.size) {
        if (Settings.tts && messages.lastIndex % 2 == 0)
            tts.speak(messages.last())
    }

    Column(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            state = lazyListState
        ) {
            itemsIndexed(messages) { i, message ->
                val scale: Animatable<Float, AnimationVector1D> =
                    remember { Animatable(0f) }

                LaunchedEffect(key1 = Unit) {
                    scale.animateTo(
                        1f,
                        animationSpec = tween(durationMillis = 500)
                    ) {
                        if (value == 1f)
                            animated.add(true)
                    }
                }

                val isBot = i % 2 == 0
                val mod = Modifier.graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value
                )

                if (isBot)
                    Message(
                        modifier = if (i == messages.lastIndex) mod else Modifier,
                        text = message,
                        color = MaterialTheme.colorSchemeExtension.bot,
                    )
                else
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(if (i == messages.lastIndex) mod else Modifier)
                    ) {
                        Message(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            text = message,
                            color = MaterialTheme.colorSchemeExtension.user,
                            isUser = true
                        )
                    }
            }
        }

        var answers by rememberMutableStateOf(value = tree.getAnswersId()
            .map { tree.getAnswerText(it) })

        ProposalList(proposals = answers) {
            answers = emptyList()
            Log.i(TAG, "Choose '$it'")
            val i = tree.getAnswersId()
                .first { i -> tree.getAnswerText(i) == it }
            messages += it
            tree.selectAnswer(i)

            crtScope.launch {
                lazyListState.animateScrollToItem(messages.size)
                delay(1.seconds)
                messages += tree.getQuestion()
                answers = tree.getAnswersId().map { i -> tree.getAnswerText(i) }
                lazyListState.animateScrollToItem(messages.size)
            }
        }

        val searchBarEnabled by rememberMutableStateOf(value = true)
        val searchBarText by rememberMutableStateOf(value = "Search")

        MySearchBar(
            placeholder = searchBarText,
            enabled = searchBarEnabled
        ) { Log.i(TAG, "Searched $it") }
    }
}
package fr.c1.chatbot.composable

import fr.c1.chatbot.composable.utils.MyText
import fr.c1.chatbot.ui.theme.ChatBotPrev
import fr.c1.chatbot.utils.UnitLaunchedEffect
import fr.c1.chatbot.utils.application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HomeLoading() = Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    val app = application
    UnitLaunchedEffect { launch(Dispatchers.Default) { app.init() } }

    CircularProgressIndicator(modifier = Modifier.size(96.dp))
    MyText(
        text = "Chargement de l'application...",
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onBackground
    )

}

@Preview
@Composable
private fun Prev() = ChatBotPrev { HomeLoading() }
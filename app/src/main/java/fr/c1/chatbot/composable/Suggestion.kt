package fr.c1.chatbot.composable

import fr.c1.chatbot.ui.theme.ChatBotPrev
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun Suggestion(modifier: Modifier = Modifier) = ToDo(name = "Afficher les suggestions de la journée")

@Preview
@Composable
private fun Prev() = ChatBotPrev {}
package fr.c1.chatbot.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun History(modifier: Modifier = Modifier) =
    ToDo(name = "Afficher l'historique des précédentes recherches")

@Preview
@Composable
private fun Prev() {
}
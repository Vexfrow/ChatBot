package fr.c1.chatbot.composable

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import fr.c1.chatbot.composable.utils.MyText

@Composable
fun History(modifier: Modifier = Modifier) =
    MyText(
        text = "Cet onglet contiendra la liste des activités choisis par l'utilisateur\nLe système visant à pouvoir choisir des activités n'étant pas implémenté, cet onglet sert à rien pour le moment",
        textAlign = TextAlign.Center
    )

@Preview
@Composable
private fun Prev() {
}
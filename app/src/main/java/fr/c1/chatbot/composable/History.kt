package fr.c1.chatbot.composable

import fr.c1.chatbot.composable.utils.MyText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

/** Component of the [Tab.History] tab, Not implemented yet */
@Composable
fun History(modifier: Modifier = Modifier) =
    MyText(
        text = "Cet onglet contiendra la liste des activités choisis par l'utilisateur\nLe système visant à pouvoir choisir des activités n'étant pas implémenté, cet onglet sert à rien pour le moment",
        textAlign = TextAlign.Center
    )
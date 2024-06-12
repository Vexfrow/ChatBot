package fr.c1.chatbot.composable

import fr.c1.chatbot.utils.application
import fr.c1.chatbot.utils.rememberMutableStateOf
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

private const val TAG = "AccountComp"

object AccountComp {
    @Composable
    fun Data(modifier: Modifier = Modifier) = Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val user = application.currentUser

            var firstName by rememberMutableStateOf(value = user.prenom)
            var lastName by rememberMutableStateOf(value = user.nom)
            var age by remember { mutableIntStateOf(user.age) }

            Row(horizontalArrangement = Arrangement.spacedBy(25.dp)) {
                TextField(
                    value = firstName,
                    onValueChange = {
                        firstName = it
                        user.prenom = it
                    },
                    label = { Text(text = "Prénom") },
                    singleLine = true
                )

                TextField(
                    value = lastName,
                    onValueChange = {
                        lastName = it
                        user.nom = it
                    },
                    label = { Text(text = "Nom de famille") },
                    singleLine = true
                )
            }

            val range = 0..120

            TextField(
                value = if (age in range) age.toString() else "",
                onValueChange = {
                    if (it.isEmpty()) {
                        age = -1
                        return@TextField
                    }

                    age = it.filter { c -> c.isDigit() }.toInt()
                    if (age < range.first)
                        age = range.first
                    else if (age > range.last)
                        age = range.last
                },
                label = { Text(text = "Âge") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
            )
        }
    }

    @Composable
    fun Preferences(modifier: Modifier = Modifier) =
        ToDo(name = "Afficher les différentes préférences hebdomadaires")
}
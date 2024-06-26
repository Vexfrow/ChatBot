package fr.c1.chatbot.composable

import fr.c1.chatbot.composable.utils.MyText
import fr.c1.chatbot.repositories.ActivitiesRepository
import fr.c1.chatbot.model.User
import fr.c1.chatbot.utils.items
import fr.c1.chatbot.utils.rememberMutableStateOf
import fr.c1.chatbot.viewModel.UserVM
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.text.Collator
import java.util.Locale

private const val TAG = "AccountComp"

/** All components of the [Tab.Account] tab */
object AccountComp {
    /**
     * Component of the [Tab.AccountData] tab
     *
     * @param user User account
     */
    @Composable
    fun Data(
        user: User,
        modifier: Modifier = Modifier
    ) = Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var firstName by rememberMutableStateOf(value = user.firstName)
            var lastName by rememberMutableStateOf(value = user.lastName)
            var age by remember { mutableIntStateOf(user.age) }

            Row(horizontalArrangement = Arrangement.spacedBy(25.dp)) {
                TextField(
                    value = firstName,
                    onValueChange = {
                        firstName = it
                        user.firstName = it.ifBlank { User.DEFAULT.firstName }
                    },
                    label = { Text(text = "Prénom") },
                    singleLine = true
                )

                TextField(
                    value = lastName,
                    onValueChange = {
                        lastName = it
                        user.lastName = it.ifBlank { User.DEFAULT.lastName }
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

                    user.age = age
                },
                label = { Text(text = "Âge") },
                keyboardActions = KeyboardActions.Default,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword
                )
            )
        }
    }

    /** Component of the [Tab.AccountPreferences] tab, not implemented yet */
    @Composable
    fun Preferences(modifier: Modifier = Modifier) =
        MyText(
            text = "Cet onglet permet à l'utilisateur de préciser ses préférences d'horaires pour chaque semaine.\n" +
                    "Par exemple, l'utilisateur peut préciser qu'il est libre tous les lundi matin\n" +
                    "Cela permet de pouvoir proposer des activités à l'utilisateur pendant les périodes marqués comme étant \"libres\" \n" +
                    "Ce n'est pas encore implémenté",
            textAlign = TextAlign.Center
        )

    /**
     * Component of the [Tab.AccountPassions] tab
     *
     * @param userVM View model to manage the users
     * @param selected Callback when a passion is selected
     * @receiver
     */
    @Composable
    fun PassionsList(
        userVM: UserVM,
        selected: (String) -> Boolean,
    ) {
        val list = remember { ActivitiesRepository.passionList }
        val selection = remember { list.map { it to selected(it) }.toMutableStateMap() }

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            val coll = Collator.getInstance(Locale.FRENCH).apply { strength = Collator.PRIMARY }
            items(list.sortedWith { left, right -> coll.compare(left, right) }) {
                FilterChip(
                    selected = selection[it]!!,
                    onClick = {
                        val newSelection = !selection[it]!!
                        selection[it] = newSelection
                        with(userVM.currentUser!!) {
                            if (newSelection) addPassion(it)
                            else removePassion(it)
                        }
                    },
                    label = {
                        Text(text = it.replaceFirstChar { c ->
                            if (c.isLowerCase()) c.titlecase(Locale.getDefault()) else c.toString()
                        }, style = MaterialTheme.typography.titleLarge)
                    })
            }
        }
    }
}
package fr.c1.chatbot.composable

import fr.c1.chatbot.model.ActivitiesRepository
import fr.c1.chatbot.ui.theme.ChatBotPrev
import fr.c1.chatbot.utils.items
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun PassionsList(
    selected: (String) -> Boolean,
    onSelectionChanged: (String, Boolean) -> Unit
) {
    val list = remember { ActivitiesRepository.passionList }
    val selection = remember { list.map { it to selected(it) }.toMutableStateMap() }

    LazyVerticalGrid(
        columns = GridCells.Fixed(6),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(list) {
            FilterChip(
                selected = selection[it]!!,
                onClick = {
                    selection[it] = !selection[it]!!
                    onSelectionChanged(it, selection[it]!!)
                },
                label = {
                    Text(text = it.replaceFirstChar { c ->
                        if (c.isLowerCase()) c.titlecase(Locale.getDefault()) else c.toString()
                    })
                })
        }
    }
}

@Preview
@Composable
private fun Prev() = ChatBotPrev { PassionsList({ true }, { _, _ -> }) }
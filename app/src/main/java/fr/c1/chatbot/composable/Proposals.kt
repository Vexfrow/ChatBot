package fr.c1.chatbot.composable

import fr.c1.chatbot.ui.theme.ChatBotPrev
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.util.Log

private const val TAG = "QuestionsList"

@Composable
fun Proposals(
    modifier: Modifier = Modifier,
    proposals: List<String>,
    onPropose: (String) -> Unit
) {
    val state = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Row {
        IconButton(
            onClick = {
                scope.launch {
                    val nextItem =
                        if (state.firstVisibleItemScrollOffset != 0) state.firstVisibleItemIndex
                        else state.firstVisibleItemIndex - 1
                    state.animateScrollToItem(nextItem)
                }
            },
            enabled = state.canScrollBackward
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                contentDescription = "Previous"
            )
        }

        LazyRow(
            modifier = modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            state = state
        ) {
            items(proposals) {
                Proposal(proposal = it) { onPropose(it) }
            }
        }

        IconButton(
            onClick = {
                scope.launch { state.animateScrollToItem(state.firstVisibleItemIndex + 1) }
            },
            enabled = state.canScrollForward
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                contentDescription = "Right"
            )
        }
    }
}

@Composable
private fun Proposal(
    proposal: String,
    onClick: () -> Unit
) = SuggestionChip(
    modifier = Modifier,
    onClick = onClick,
    label = {
        Text(
            modifier = Modifier.padding(vertical = 5.dp),
            text = proposal,
            style = MaterialTheme.typography.titleLarge
        )
    }
)

@Preview
@Composable
private fun Prev() = ChatBotPrev {
    Proposals(
        proposals = (0..20).map { "proposal $it" }
    ) { Log.i(TAG, "Prev: $it choosed") }
}
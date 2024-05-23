package fr.c1.chatbot.composable

import fr.c1.chatbot.model.Event
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EventList(events: List<Event>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(events) { event ->
            EventItem(event = event)
        }
    }
}

@Composable
fun EventItem(event: Event) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val startTime = dateFormat.format(Date(event.dtStart))
    val endTime = dateFormat.format(Date(event.dtEnd))

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = event.title, style = MaterialTheme.typography.titleLarge)
        Text(text = "Début : $startTime", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Fin : $endTime", style = MaterialTheme.typography.bodyMedium)
    }
}
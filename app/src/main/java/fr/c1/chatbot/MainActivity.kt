package fr.c1.chatbot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import fr.c1.chatbot.ui.theme.ChatBotTheme
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.CalendarContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val PERMISSIONS_REQUEST_READ_CALENDAR = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestCalendarPermission()
        enableEdgeToEdge()
        setContent {
            ChatBotTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (!hasCalendarPermission(this)) {
                        requestCalendarPermission()
                    } else {
                        val events = fetchCalendarEvents(this)
                        EventList(events, Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }

    private fun hasCalendarPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_CALENDAR
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCalendarPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.READ_CALENDAR),
            PERMISSIONS_REQUEST_READ_CALENDAR
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_READ_CALENDAR -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    // La permission est accordée, récupérer les événements du calendrier
                    val events = fetchCalendarEvents(this)
                    setContent {
                        ChatBotTheme {
                            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                                EventList(events, Modifier.padding(innerPadding))
                                Text(
                                    "Permission granted",
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                        }
                    }
                } else {
                    // La permission est refusée, ne rien faire
                }
                return
            }
        }
    }


    data class Event(val title: String, val dtStart: Long, val dtEnd: Long)

    private fun fetchCalendarEvents(context: Context): List<Event> {
        val events = mutableListOf<Event>()
        val projection = arrayOf(
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND
        )
        val uri: Uri = CalendarContract.Events.CONTENT_URI
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val titleIndex = it.getColumnIndexOrThrow(CalendarContract.Events.TITLE)
            val dtStartIndex = it.getColumnIndexOrThrow(CalendarContract.Events.DTSTART)
            val dtEndIndex = it.getColumnIndexOrThrow(CalendarContract.Events.DTEND)
            while (it.moveToNext()) {
                val title = it.getString(titleIndex)
                val dtStart = it.getLong(dtStartIndex)
                val dtEnd = it.getLong(dtEndIndex)
                events.add(Event(title, dtStart, dtEnd))
            }
        }
        return events
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ChatBotTheme {
        Greeting("Android")
    }
}

@Composable
fun EventList(events: List<MainActivity.Event>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(events) { event ->
            EventItem(event = event)
        }
    }
}

@Composable
fun EventItem(event: MainActivity.Event) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val startTime = dateFormat.format(Date(event.dtStart))
    val endTime = dateFormat.format(Date(event.dtEnd))

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = event.title, style = MaterialTheme.typography.titleLarge)
        Text(text = "Début : $startTime", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Fin : $endTime", style = MaterialTheme.typography.bodyMedium)
    }
}
package fr.c1.chatbot

import fr.c1.chatbot.utils.*
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
import android.content.pm.PackageManager

class MainActivity : ComponentActivity() {
    var calendar1 = Calendar()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatBotTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (!calendar1.hasReadCalendarPermission(this)) {
                        requestReadCalendarPermission()
                    } else {
                        val events = calendar1.fetchCalendarEvents(this)
                        EventList(events, Modifier.padding(innerPadding))
                    }
                    if (!calendar1.hasWriteCalendarPermission(this)) {
                        requestWriteCalendarPermission()
                    } else {
                        val startMillis: Long = System.currentTimeMillis()
                        val endMillis: Long = startMillis + 3600000 // 1 heure
                        val event = Event("Test", startMillis, endMillis)
                        calendar1.writeEvent(this, event)
                        val events = calendar1.fetchCalendarEvents(this)
                        EventList(events, Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }

    private fun requestReadCalendarPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.READ_CALENDAR),
            calendar1.PERMISSIONS_REQUEST_READ_CALENDAR
        )
    }

    private fun requestWriteCalendarPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.WRITE_CALENDAR),
            calendar1.PERMISSIONS_REQUEST_WRITE_CALENDAR
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            calendar1.PERMISSIONS_REQUEST_READ_CALENDAR -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    // La permission est accordée, récupérer les événements du calendrier
                    val events = calendar1.fetchCalendarEvents(this)
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
            } calendar1.PERMISSIONS_REQUEST_WRITE_CALENDAR -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    // La permission est accordée, écrire un événement dans le calendrier
                    val startMillis: Long = System.currentTimeMillis()
                    val endMillis: Long = startMillis + 3600000 // 1 heure
                    val event = Event("Test", startMillis, endMillis)
                    calendar1.writeEvent(this, event)
                    setContent {
                        ChatBotTheme {
                            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                                Text(
                                    "Permission granted",
                                    modifier = Modifier.padding(innerPadding)
                                )
                                val events = calendar1.fetchCalendarEvents(this)
                                EventList(events, Modifier.padding(innerPadding))
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
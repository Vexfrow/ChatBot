package fr.c1.chatbot

import fr.c1.chatbot.composable.EventList
import fr.c1.chatbot.model.Event
import fr.c1.chatbot.ui.theme.ChatBotTheme
import fr.c1.chatbot.utils.Calendar
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
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatBotTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (!Calendar.PermissionsRequest.hasReadCalendarPermission(this)) {
                        Calendar.PermissionsRequest.requestCalendarPermissions(this)
                    } else {
                        val events = Calendar.fetchCalendarEvents(this)
                        EventList(events, Modifier.padding(innerPadding))
                    }
                    if (!Calendar.PermissionsRequest.hasWriteCalendarPermission(this)) {
                        // TODO : synchroniser les demandes de permissions
                        Calendar.PermissionsRequest.requestCalendarPermissions(this)
                    } else {
                        val startMillis: Long = System.currentTimeMillis()
                        val endMillis: Long = startMillis + 3600000 // 1 heure
                        val event = Event("Test12345 permission", startMillis, endMillis)
                        Calendar.writeEvent(this, event)
                        val events = Calendar.fetchCalendarEvents(this)
                        EventList(events, Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            Calendar.PermissionsRequest.REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // La permission est accordée
                    // Créer un évenement dans le calendrier
                    val startMillis: Long = System.currentTimeMillis()
                    val endMillis: Long = startMillis + 3600000 // 1 heure
                    val event = Event("Test12345 pas de permission", startMillis, endMillis)
                    Calendar.writeEvent(this, event)
                    // Récupérer les événements du calendrier
                    val events = Calendar.fetchCalendarEvents(this)
                    setContent {
                        ChatBotTheme {
                            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                                EventList(events, Modifier.padding(innerPadding))
                            }
                        }
                    }
                } else {
                    // La permission est refusée, ne rien faire
                    Log.e(TAG, "Calendar permission denied")
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
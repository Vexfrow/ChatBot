package fr.c1.chatbot

import android.app.Activity
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
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import fr.c1.chatbot.utils.Calendar.PermissionsRequest.hasReadCalendarPermission
import fr.c1.chatbot.utils.Calendar.PermissionsRequest.hasWriteCalendarPermission
import fr.c1.chatbot.utils.Calendar.fetchCalendarEvents

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatBotTheme {
                PermissionsContent()
            }
        }
    }
}

@Composable
fun PermissionsContent() {
    val context = LocalContext.current

    var hasReadPermission by remember { mutableStateOf(false) }
    var hasWritePermission by remember { mutableStateOf(false) }
    var events by remember { mutableStateOf<List<Event>>(emptyList()) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasReadPermission = permissions[android.Manifest.permission.READ_CALENDAR] == true
        hasWritePermission = permissions[android.Manifest.permission.WRITE_CALENDAR] == true
        if (hasReadPermission && hasWritePermission) {
            events = fetchCalendarEvents(context)
        }
    }

    LaunchedEffect(Unit) {
        if (!hasReadCalendarPermission(context) || !hasWriteCalendarPermission(context)) {
            requestPermissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.READ_CALENDAR,
                    android.Manifest.permission.WRITE_CALENDAR
                )
            )
        } else {
            hasReadPermission = true
            hasWritePermission = true
            events = fetchCalendarEvents(context)
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        if (hasReadPermission && hasWritePermission) {
            val startMillis: Long = System.currentTimeMillis()
            val endMillis: Long = startMillis + 3600000 // 1 heure
            val event = Event("Test123456 permission", startMillis, endMillis)
            Calendar.writeEvent(context, event)
            EventList(events, Modifier.padding(innerPadding))
        } else {
            Text("Requesting permissions...", modifier = Modifier.padding(innerPadding))
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
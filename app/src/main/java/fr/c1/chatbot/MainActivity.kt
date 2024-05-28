package fr.c1.chatbot

import android.Manifest
import fr.c1.chatbot.composable.EventList
import fr.c1.chatbot.model.Event
import fr.c1.chatbot.ui.theme.ChatBotTheme
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
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.work.WorkManager
import fr.c1.chatbot.model.toDate
import fr.c1.chatbot.utils.Calendar
import fr.c1.chatbot.utils.Calendar.PermissionsRequest.hasReadCalendarPermission
import fr.c1.chatbot.utils.Calendar.PermissionsRequest.hasWriteCalendarPermission
import fr.c1.chatbot.utils.Calendar.fetchCalendarEvents
import fr.c1.chatbot.utils.scheduleEventReminders

private const val TAG = "MainActivity"

private var initNotif = false

class MainActivity : ComponentActivity() {

    private lateinit var workManager: WorkManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        workManager = WorkManager.getInstance(this)
        enableEdgeToEdge()
        setContent {
            ChatBotTheme {
                PermissionsContent()
                PermissionNotification()
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
            hasReadPermission = permissions[Manifest.permission.READ_CALENDAR] == true
            hasWritePermission = permissions[Manifest.permission.WRITE_CALENDAR] == true
        }

        LaunchedEffect(Unit) {
            if (!hasReadCalendarPermission(context) || !hasWriteCalendarPermission(context)) {
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.READ_CALENDAR,
                        Manifest.permission.WRITE_CALENDAR
                    )
                )
            } else {
                hasReadPermission = true
                hasWritePermission = true
            }
        }

        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            if (hasReadPermission && hasWritePermission) {
                events = fetchCalendarEvents(context)
                addNotifPush(events)
                //EventList(events, Modifier.padding(innerPadding))
            } else {
                Text("Requesting permissions...", modifier = Modifier.padding(innerPadding))
            }
        }
    }

    private fun addNotifPush(events: List<Event>) {
        if (initNotif) return
        workManager.cancelAllWork()
        Log.d(TAG, "onCreate: cancelAllWork()")
        for (event in events) {
            if (event.dtStart >= System.currentTimeMillis() + (1000 * 60 * 60)) { // Si l'event est dans le futur (dans 1h minimum)
                Log.i(TAG, "onCreate: ${event.title}")
                scheduleEventReminders(this, event.title, event.dtStart)
            }
        }
        // Afficher tous les notifs programmées (log)
        workManager.getWorkInfosByTagLiveData("EventReminderWorker")
            .observe(this) { workInfos ->
                for (workInfo in workInfos) {
                    if (workInfo.state.isFinished) {
                        Log.d(
                            TAG,
                            "onCreate: delete : ${workInfo.id}, ${workInfo.state}, ${workInfo.nextScheduleTimeMillis.toDate()}"
                        )
                        workManager.cancelWorkById(workInfo.id)
                    } else {
                        Log.i(
                            TAG,
                            "onCreate: Notif ajoutée : ${workInfo.id}, ${workInfo.state}, exécution programmée le ${workInfo.nextScheduleTimeMillis.toDate()}"
                        )
                    }
                }
            }
        initNotif = true
    }

    private fun addEvent(events: List<Event>) {
        val startMillis: Long = System.currentTimeMillis() + 1000 * 60 * 60 // Actual Time + 1 heure
        val endMillis: Long = startMillis + 3600000 // + 1 heure
        Calendar.writeEvent(this, "Test avec permission", startMillis, endMillis, events)
        initNotif = false
    }

    @Composable
    fun PermissionNotification() {
        val requestPermissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[Manifest.permission.RECEIVE_BOOT_COMPLETED] == true &&
                permissions[Manifest.permission.POST_NOTIFICATIONS] == true
            ) {
                Log.i(TAG, "Notifications Permissions granted")
            } else {
                Log.i(TAG, "Notifications Permissions denied")
            }
        }

        LaunchedEffect(Unit) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.RECEIVE_BOOT_COMPLETED,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            )
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
}
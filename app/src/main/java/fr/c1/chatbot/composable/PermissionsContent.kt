package fr.c1.chatbot.composable

import fr.c1.chatbot.model.Event
import fr.c1.chatbot.utils.Calendar
import fr.c1.chatbot.utils.LocationHandler
import fr.c1.chatbot.utils.hasPermission
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import android.Manifest
import android.os.Build
import android.util.Log

private const val TAG = "PermissionComponent"

/**
 * Composable that handles permissions requests
 */
@Composable
fun PermissionsContent(context: ComponentActivity) {
    var hasReadPermission by remember { mutableStateOf(false) }
    var hasWritePermission by remember { mutableStateOf(false) }
    var events by remember { mutableStateOf<List<Event>>(emptyList()) }

    var hasFineLocation by remember { mutableStateOf(false) }
    var hasCoarseLocation by remember { mutableStateOf(false) }

    var locationRequesting by remember { mutableStateOf(false) }

    var initNotif by remember { mutableStateOf(false) }

    var permissionsArray: Array<String> = arrayOf()

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasReadPermission = permissions[Manifest.permission.READ_CALENDAR] == true
        hasWritePermission = permissions[Manifest.permission.WRITE_CALENDAR] == true
        hasFineLocation = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        hasCoarseLocation = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    LaunchedEffect(Unit) {
        if (!Calendar.PermissionsRequest.hasReadCalendarPermission(context) || !Calendar.PermissionsRequest.hasWriteCalendarPermission(
                context
            )
        ) {
            // Array of permissions to request
            permissionsArray = permissionsArray.plus(Manifest.permission.READ_CALENDAR)
            permissionsArray = permissionsArray.plus(Manifest.permission.WRITE_CALENDAR)
        } else {
            hasReadPermission = true
            hasWritePermission = true
        }
        if (!context.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) || !context.hasPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            permissionsArray = permissionsArray.plus(Manifest.permission.ACCESS_FINE_LOCATION)
            permissionsArray = permissionsArray.plus(Manifest.permission.ACCESS_COARSE_LOCATION)
        } else {
            hasFineLocation = true
            hasCoarseLocation = true
        }
        if (!context.hasPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED) || !context.hasPermission(
                Manifest.permission.POST_NOTIFICATIONS
            )
        ) {
            permissionsArray = permissionsArray.plus(Manifest.permission.RECEIVE_BOOT_COMPLETED)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionsArray = permissionsArray.plus(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            Log.i(TAG, "PermissionsContent: Notifications permissions granted")
            initNotif = true
        }
        if (permissionsArray.isNotEmpty()) {
            requestPermissionLauncher.launch(permissionsArray)
        }
    }

    LaunchedEffect(hasReadPermission && hasWritePermission) {
        if (hasReadPermission && hasWritePermission) {
            events = Calendar.fetchCalendarEvents(context)
        } else {
            Log.d(TAG, "PermissionsContent: Calendar permissions not granted")
        }
    }

    LaunchedEffect(hasFineLocation && hasCoarseLocation) {
        if (hasFineLocation && hasCoarseLocation) {
            if (!locationRequesting) {
                LocationHandler.initLocation(context)
                //locationHandler.startLocationUpdates()
            }
        } else {
            Log.d(TAG, "PermissionsContent: Location permissions not granted")
        }
    }

    LaunchedEffect(initNotif) {
        if (initNotif)
            Event.Notifs.addNotification(events, context)
        else
            Log.d(TAG, "PermissionsContent: Notifications permissions not granted")
    }
}
package fr.c1.chatbot.utils

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.CalendarContract
import android.util.Log
import androidx.core.content.ContextCompat

private const val TAG = "Calendar"

class Calendar {
    val PERMISSIONS_REQUEST_CALENDAR = 100

    /**
     * Vérifie si l'application a la permission de lire le calendrier
     */
    fun hasReadCalendarPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_CALENDAR
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Vérifie si l'application a la permission d'écrire dans le calendrier
     */
    fun hasWriteCalendarPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_CALENDAR
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Récupère les événements du calendrier
     */
    fun fetchCalendarEvents(context: Context): List<Event> {
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

    /**
     * Ecrit un événement dans le calendrier
     */
    fun writeEvent(context: Context, event: Event) {
        val beginTime = event.dtStart
        val endTime = event.dtEnd
        val title = event.title

        // Récupérer l'ide du calendrier
        val calendarId = getCalendarId(context)

        val values = ContentValues().apply {
            put(CalendarContract.Events.CALENDAR_ID, calendarId)
            put(CalendarContract.Events.DTSTART, beginTime)
            put(CalendarContract.Events.DTEND, endTime)
            put(CalendarContract.Events.TITLE, title)
            // TimeZone FR
            put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Paris")
        }
        val contentResolver = context.contentResolver
        contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
        Log.i(TAG, "writeEvent: Event added to calendar")
    }

    /**
     * Récupérer l'id des calendriers
     */
    private fun getCalendarId(context: Context): Long {
        val projection = arrayOf(
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME
        )
        val uri: Uri = CalendarContract.Calendars.CONTENT_URI
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        // Récupérer l'id des calendriers disponibles
        cursor?.use {
            val idIndex = it.getColumnIndexOrThrow(CalendarContract.Calendars._ID)
            val nameIndex =
                it.getColumnIndexOrThrow(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME)
            // Choisir le calendrier "My Calendar"
            while (it.moveToNext()) {
                val id = it.getLong(idIndex)
                val name = it.getString(nameIndex)
                Log.d("Calendar", "id: $id, name: $name")
                if (name == "My Calendar") {
                    return id
                }
            }
        }
        Log.d(TAG, "getCalendarId: My Calendar not found")
        return 1
    }

}
package fr.c1.chatbot.utils

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.CalendarContract
import androidx.core.content.ContextCompat

class Calendar {
    val PERMISSIONS_REQUEST_READ_CALENDAR = 100
    val PERMISSIONS_REQUEST_WRITE_CALENDAR = 101

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

        val values = ContentValues().apply {
            put(CalendarContract.Events.CALENDAR_ID, 1)
            put(CalendarContract.Events.DTSTART, beginTime)
            put(CalendarContract.Events.DTEND, endTime)
            put(CalendarContract.Events.TITLE, title)
            // TimeZone FR
            put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Paris")
        }
        val contentResolver = context.contentResolver
        contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
        System.err.println("Event added")
    }
}
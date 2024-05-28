package fr.c1.chatbot.utils

import fr.c1.chatbot.model.Event
import fr.c1.chatbot.utils.Calendar.PermissionsRequest.hasReadCalendarPermission
import fr.c1.chatbot.utils.Calendar.PermissionsRequest.hasWriteCalendarPermission
import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.CalendarContract
import android.util.Log
import fr.c1.chatbot.composable.getNewID
import fr.c1.chatbot.model.toDate

private const val TAG = "Calendar"

/** An object to manage the calendar */
object Calendar {
    /** Codes of the permission requests */
    object PermissionsRequest {
        /** Permission to read the calendar */
        const val REQUEST_CODE = 100

        /**
         * Vérifie si l'application a la permission de lire le calendrier
         */
        fun hasReadCalendarPermission(context: Context): Boolean =
            context.hasPermission(Manifest.permission.READ_CALENDAR)

        /**
         * Vérifie si l'application a la permission d'écrire dans le calendrier
         */
        fun hasWriteCalendarPermission(context: Context): Boolean =
            context.hasPermission(Manifest.permission.WRITE_CALENDAR)

        fun requestCalendarPermissions(activity: Activity) {
            activity.requestPermissions(
                arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR),
                REQUEST_CODE
            )
        }
    }

    /**
     * Récupère les événements du calendrier
     */
    fun fetchCalendarEvents(context: Context): List<Event> {
        if (!hasReadCalendarPermission(context)) {
            Log.i(TAG, "fetchCalendarEvents: No permission to read calendar")
            return emptyList()
        }
        val events = mutableListOf<Event>()
        val projection = arrayOf(
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.DELETED
        )
        val selection = "${CalendarContract.Events.DELETED} = ?"
        val selectionArgs = arrayOf("0") // Récupérer uniquement les événements non supprimés
        val sortOrder = "${CalendarContract.Events.DTSTART} ASC"
        val uri: Uri = CalendarContract.Events.CONTENT_URI
        val cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)
        cursor?.use {
            val idIndex = it.getColumnIndexOrThrow(CalendarContract.Events._ID)
            val titleIndex = it.getColumnIndexOrThrow(CalendarContract.Events.TITLE)
            val dtStartIndex = it.getColumnIndexOrThrow(CalendarContract.Events.DTSTART)
            val dtEndIndex = it.getColumnIndexOrThrow(CalendarContract.Events.DTEND)
            val deletedIndex = it.getColumnIndexOrThrow(CalendarContract.Events.DELETED)
            while (it.moveToNext()) {
                val id = it.getLong(idIndex)
                val title = it.getString(titleIndex)
                val dtStart = it.getLong(dtStartIndex)
                val dtEnd = it.getLong(dtEndIndex)
                val deleted = it.getInt(deletedIndex)
                if (deleted == 0) { // Vérifie que l'événement n'est pas marqué comme supprimé
                    events.add(Event(id, title, dtStart, dtEnd))
                }
            }
        }
        return events
    }

    /**
     * Ecrit un événement dans le calendrier
     */
    private fun writeEvent(context: Context, event: Event) {
        if (!hasWriteCalendarPermission(context)) {
            Log.i(TAG, "writeEvent: No permission to write in calendar")
            return
        }
        val beginTime = event.dtStart
        val endTime = event.dtEnd
        val title = event.title

        // Récupérer l'ide du calendrier
        val calendarId = getCalendarId(context)

        if (calendarId == -1L) {
            Log.e(TAG, "writeEvent: No one calendar in edit mode!")
            return
        }

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
        Log.i(TAG, "writeEvent: Event added to calendar $calendarId")
    }

    fun writeEvent(context: Context, title: String, startMillis: Long, endMillis: Long, events: List<Event>) {
        val id = getNewID(events)
        val event = Event(id, title, startMillis, endMillis)
        writeEvent(context, event)
    }

    /**
     * Récupérer l'id des calendriers
     */
    private fun getCalendarId(context: Context): Long {
        val projection = arrayOf(
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
            CalendarContract.Calendars.IS_PRIMARY
        )
        val uri: Uri = CalendarContract.Calendars.CONTENT_URI
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        // Récupérer l'id des calendriers disponibles
        cursor?.use {
            val idIndex = it.getColumnIndexOrThrow(CalendarContract.Calendars._ID)
            val nameIndex =
                it.getColumnIndexOrThrow(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME)
            val primaryIndex = it.getColumnIndexOrThrow(CalendarContract.Calendars.IS_PRIMARY)
            // Choisir le calendrier "primaire"
            while (it.moveToNext()) {
                val id = it.getLong(idIndex)
                val name = it.getString(nameIndex)
                val primary = it.getInt(primaryIndex)

                Log.d("Calendar", "id: $id, name: $name, prim: $primary")

                if (primary == 1) {
                    return id
                }
            }
        }
        return -1L
    }

    fun deleteAllDayEvents(context: Context) {
        val projection = arrayOf(
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.DELETED
        )
        val selection = "${CalendarContract.Events.DELETED} = ?"
        val selectionArgs = arrayOf("0") // Récupérer uniquement les événements non supprimés
        val sortOrder = "${CalendarContract.Events.DTSTART} ASC"
        val uri: Uri = CalendarContract.Events.CONTENT_URI
        val cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)
        cursor?.use {
            val idIndex = it.getColumnIndexOrThrow(CalendarContract.Events._ID)
            val dtStartIndex = it.getColumnIndexOrThrow(CalendarContract.Events.DTSTART)
            val deletedIndex = it.getColumnIndexOrThrow(CalendarContract.Events.DELETED)
            while (it.moveToNext()) {
                val id = it.getLong(idIndex)
                val dtStart = it.getLong(dtStartIndex)
                val deleted = it.getInt(deletedIndex)
                if (deleted == 0) { // Vérifie que l'événement n'est pas marqué comme supprimé
                    if (isAllDayEvent(dtStart)) {
                        deleteEvent(context, id)
                    }
                }
            }
        }
    }

    private fun isAllDayEvent(dtStart: Long): Boolean {
        val date = dtStart.toDate()
        Log.d(TAG, "isAllDayEvent: ${date.substring(0, 9)}")
        val res = date.substring(0, 10) == "27/05/2024"
        return res
    }

    private fun deleteEvent(context: Context, eventId: Long) {
        val uri = CalendarContract.Events.CONTENT_URI
        val selection = "${CalendarContract.Events._ID} = ?"
        val selectionArgs = arrayOf(eventId.toString())
        context.contentResolver.delete(uri, selection, selectionArgs)
    }

}
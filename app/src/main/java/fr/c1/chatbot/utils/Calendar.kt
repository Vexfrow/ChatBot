package fr.c1.chatbot.utils

import fr.c1.chatbot.model.messageManager.Event
import fr.c1.chatbot.utils.Calendar.PermissionsRequest.hasReadCalendarPermission
import fr.c1.chatbot.utils.Calendar.PermissionsRequest.hasWriteCalendarPermission
import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.CalendarContract
import android.util.Log

private const val TAG = "Calendar"

/** An object to manage the calendar */
object Calendar {
    /** Codes of the permission requests */
    object PermissionsRequest {
        /** Permission to read the calendar */
        const val REQUEST_CODE = 100

        /**
         * Check if the application has the permission to read the calendar
         */
        fun hasReadCalendarPermission(context: Context): Boolean =
            context.hasPermission(Manifest.permission.READ_CALENDAR)

        /**
         * Check if the application has the permission to write in the calendar
         */
        fun hasWriteCalendarPermission(context: Context): Boolean =
            context.hasPermission(Manifest.permission.WRITE_CALENDAR)
    }

    /**
     * Fetch the events from the calendar
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
        val selection =
            "${CalendarContract.Events.DELETED} = ? AND ${CalendarContract.Events.CALENDAR_ID} = ?"
        // Check if the calendar of id 99 exists
        val calendarId = getCalendarId(context)
        if (calendarId == -1L) {
            Log.e(TAG, "fetchCalendarEvents: No ChatBot calendar !")
            createCalendar(context)
        }
        // Fetch only the non-deleted events from the ChatBot calendar
        val selectionArgs = arrayOf("0", "99")
        val sortOrder = "${CalendarContract.Events.DTSTART} ASC"
        val uri: Uri = CalendarContract.Events.CONTENT_URI
        val cursor =
            context.contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)
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
     * Write an event in the calendar
     */
    private fun writeEvent(context: Context, event: Event) {
        if (!hasWriteCalendarPermission(context)) {
            Log.i(TAG, "writeEvent: No permission to write in calendar")
            return
        }
        val beginTime = event.dtStart
        val endTime = event.dtEnd
        val title = event.title

        // Get the id of the ChatBot calendar
        val calendarId = getCalendarId(context)

        if (calendarId == -1L) {
            Log.e(TAG, "writeEvent: No ChatBot calendar !")
            createCalendar(context)
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

    /**
     * Write an event in the calendar
     * @param context The context
     * @param title The title of the event
     * @param startMillis The start time of the event
     * @param endMillis The end time of the event
     * @param events The list of events
     */
    fun writeEvent(
        context: Context,
        title: String,
        startMillis: Long,
        endMillis: Long,
        events: List<Event>
    ) {
        val id = getNewID(events)
        val event = Event(id, title, startMillis, endMillis)
        writeEvent(context, event)
    }

    /**
     * Create a calendar of id 99
     * @param context The context
     */
    fun createCalendar(context: Context): Long? {
        val accountName = "ChatBot"
        val accountType = CalendarContract.ACCOUNT_TYPE_LOCAL

        val values = ContentValues().apply {
            put(CalendarContract.Calendars.ACCOUNT_NAME, accountName)
            put(CalendarContract.Calendars.ACCOUNT_TYPE, accountType)
            put(CalendarContract.Calendars.NAME, "ChatBot")
            put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "ChatBot")
            put(CalendarContract.Calendars.CALENDAR_COLOR, -0x10000)
            put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER)
            put(CalendarContract.Calendars.SYNC_EVENTS, 1)
            put(CalendarContract.Calendars.VISIBLE, 1)
            put(CalendarContract.Calendars._ID, 99)
        }

        val uri = CalendarContract.Calendars.CONTENT_URI
        val calendarUri = uri.buildUpon()
            .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
            .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, accountName)
            .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, accountType)
            .build()

        val resultUri = context.contentResolver.insert(calendarUri, values)
        return resultUri?.lastPathSegment?.toLong()
    }

    /**
     * Delete a calendar
     * @param context The context
     * @param id The id of the calendar to delete
     */
    fun deleteCalendar(context: Context, id: Long) {
        val uri = CalendarContract.Calendars.CONTENT_URI
        val selection = "${CalendarContract.Calendars._ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        context.contentResolver.delete(uri, selection, selectionArgs)
    }

    /**
     * Get the id of the ChatBot calendar
     */
    private fun getCalendarId(context: Context): Long {
        val projection = arrayOf(
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
            CalendarContract.Calendars.IS_PRIMARY
        )
        val uri: Uri = CalendarContract.Calendars.CONTENT_URI
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        // Get the id of the ChatBot calendar
        cursor?.use {
            val idIndex = it.getColumnIndexOrThrow(CalendarContract.Calendars._ID)
            val nameIndex =
                it.getColumnIndexOrThrow(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME)
            val primaryIndex = it.getColumnIndexOrThrow(CalendarContract.Calendars.IS_PRIMARY)
            // Chooses the ChatBot calendar if it exists
            while (it.moveToNext()) {
                val id = it.getLong(idIndex)
                val name = it.getString(nameIndex)
                val primary = it.getInt(primaryIndex)

                Log.d("Calendar", "id: $id, name: $name, prim: $primary")

                if (id == 99L && name.equals("ChatBot")) {
                    return id
                }
            }
        }
        return -1L
    }

    /**
     * Delete all the all-day events of the calendar
     * @param context The context
     */
    fun deleteAllDayEvents(context: Context) {
        val projection = arrayOf(
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.DELETED
        )
        val selection =
            "${CalendarContract.Events.DELETED} = ? AND ${CalendarContract.Events.CALENDAR_ID} = ?"
        val selectionArgs = arrayOf("0", "99") // Récupérer uniquement les événements non supprimés
        val sortOrder = "${CalendarContract.Events.DTSTART} ASC"
        val uri: Uri = CalendarContract.Events.CONTENT_URI
        val cursor =
            context.contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)
        cursor?.use {
            val idIndex = it.getColumnIndexOrThrow(CalendarContract.Events._ID)
            val dtStartIndex = it.getColumnIndexOrThrow(CalendarContract.Events.DTSTART)
            val deletedIndex = it.getColumnIndexOrThrow(CalendarContract.Events.DELETED)
            while (it.moveToNext()) {
                val id = it.getLong(idIndex)
                val dtStart = it.getLong(dtStartIndex)
                val deleted = it.getInt(deletedIndex)
                if (deleted == 0) { // Check if the event is not marked as deleted
                    if (isAllDayEvent(dtStart)) {
                        deleteEvent(context, id)
                    }
                }
            }
        }
    }

    /**
     * Check if the event is an all-day event
     */
    private fun isAllDayEvent(dtStart: Long): Boolean {
        val date = dtStart.toDate()
        Log.d(TAG, "isAllDayEvent: ${date.substring(0, 10)}")
        // Date of the day
        val today = System.currentTimeMillis().toDate().substring(0, 10)
        return date.substring(0, 10) == today
    }

    /**
     * Delete an event from the calendar
     * @param context The context
     * @param eventId The id of the event to delete
     */
    private fun deleteEvent(context: Context, eventId: Long) {
        val uri = CalendarContract.Events.CONTENT_URI
        val selection = "${CalendarContract.Events._ID} = ?"
        val selectionArgs = arrayOf(eventId.toString())
        context.contentResolver.delete(uri, selection, selectionArgs)
    }

    /**
     * Get a new id for an event
     */
    private fun getNewID(events: List<Event>): Long {
        return events.maxOfOrNull { it.id }?.plus(1) ?: 0
    }
}
package fr.c1.chatbot.model.messageManager

import fr.c1.chatbot.utils.EventReminderWorker
import fr.c1.chatbot.utils.toDate
import androidx.activity.ComponentActivity
import androidx.work.WorkManager
import android.util.Log

/**
 * Represent an event
 *
 * @property id Event ID
 * @property title Event title
 * @property dtStart Event start date
 * @property dtEnd Event end date
 * @constructor Create Event with all its properties
 */
data class Event(
    val id: Long,
    val title: String,
    val dtStart: Long,
    val dtEnd: Long
) {
    /** Utility class to manage event notification */
    object Notifs {
        private const val TAG = "Event.Notifs"

        /** Indicate if the notification system is initialized */
        private var inited: Boolean = true

        /**
         * Add a notification
         *
         * @param events Event corresponding to the notification
         * @param ctx Current Android context
         */
        fun addNotification(events: List<Event>, ctx: ComponentActivity) {
            val workManager = WorkManager.getInstance(ctx)
            if (inited) return
            workManager.cancelAllWorkByTag("EventReminderWorker")
            Log.d(TAG, "onCreate: cancelAllWork()")
            for (event in events) {
                if (event.dtStart >= System.currentTimeMillis() + (1000 * 60 * 60)) { // If event is in the future (in more than 1 hour)
                    Log.i(TAG, "onCreate: ${event.title}")
                    EventReminderWorker.scheduleEventReminders(ctx, event.title, event.dtStart)
                }
            }
            // Display all scheduled notifications
            workManager.getWorkInfosByTagLiveData("EventReminderWorker")
                .observe(ctx) { workInfos ->
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
            inited = true
        }
    }
}
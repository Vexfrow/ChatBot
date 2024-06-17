package fr.c1.chatbot.utils

import fr.c1.chatbot.R
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import java.util.concurrent.TimeUnit

private const val TAG = "EventReminderWorker"

class EventReminderWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    companion object {
        const val NOTIFICATION_ID = 123
        const val CHANNEL_ID = "event_reminder"

        fun scheduleEventReminders(context: Context, eventTitle: String, eventStartTime: Long) {
            val currentTime = System.currentTimeMillis()
            val oneDayBefore = eventStartTime - TimeUnit.HOURS.toMillis(24)
            val oneHourBefore = eventStartTime - TimeUnit.HOURS.toMillis(1)
            var oneDayBeforeWorkRequest: PeriodicWorkRequest
            var oneHourBeforeWorkRequest: PeriodicWorkRequest

            if (oneDayBefore >= currentTime) {
                val oneDayBeforeRequest = Data.Builder()
                    .putString("title", eventTitle)
                    .putLong("eventStartTime", eventStartTime)
                    .build()
                oneDayBeforeWorkRequest = PeriodicWorkRequestBuilder<EventReminderWorker>(
                    1, TimeUnit.DAYS,
                    1439, TimeUnit.MINUTES,
                )
                    .setInitialDelay(oneDayBefore - currentTime, TimeUnit.MILLISECONDS)
                    .setInputData(oneDayBeforeRequest)
                    .addTag(TAG)
                    .build()
                WorkManager.getInstance(context).enqueue(oneDayBeforeWorkRequest)
            }

            if (oneHourBefore >= currentTime) {
                val oneHourBeforeRequest = Data.Builder()
                    .putString("title", eventTitle)
                    .putLong("eventStartTime", eventStartTime)
                    .build()
                oneHourBeforeWorkRequest = PeriodicWorkRequestBuilder<EventReminderWorker>(
                    1, TimeUnit.HOURS,
                    59, TimeUnit.MINUTES,
                )
                    .setInitialDelay(oneHourBefore - currentTime, TimeUnit.MILLISECONDS)
                    .setInputData(oneHourBeforeRequest)
                    .addTag(TAG)
                    .build()
                WorkManager.getInstance(context).enqueue(oneHourBeforeWorkRequest)
            }
        }
    }

    override suspend fun doWork(): Result {
        val title = inputData.getString("title")

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Rappels d'événement",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(applicationContext, "event_reminder")
            .setContentTitle("Rappel d'événement")
            .setContentText(title)
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)

        return Result.success()
    }
}
package fr.c1.chatbot.model

/**
 * Weekly preference
 *
 * @property day Day of the week
 * @property hour Hour of the day
 * @property duration Duration of the activity
 * @constructor Creates a weekly preference with the specified parameters
 */
class WeeklyPreference(
    val day: String,
    val hour: String,
    val duration: Int
)
package fr.c1.chatbot.model

/**
 * Weekly preference
 *
 * @property day
 * @property hour
 * @property duration
 * @constructor Create Weekly preference
 */
class WeeklyPreference(
    val day: String,
    val hour: String,
    val duration: Int
)
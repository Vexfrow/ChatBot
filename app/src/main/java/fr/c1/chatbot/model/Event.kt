package fr.c1.chatbot.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Event(
    val id: Long,
    val title: String,
    val dtStart: Long,
    val dtEnd: Long
)

// Convertit un long en date
fun Long.toDate(): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return dateFormat.format(Date(this))
}
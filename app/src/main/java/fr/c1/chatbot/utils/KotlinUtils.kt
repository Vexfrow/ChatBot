package fr.c1.chatbot.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Int.toBool(): Boolean = this != 0
fun Boolean.toInt(): Int = if (this) 1 else 0
fun Boolean.toFloat(): Float = if (this) 1f else 0f

// Convertit un long en date
fun Long.toDate(): String = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    .format(Date(this))
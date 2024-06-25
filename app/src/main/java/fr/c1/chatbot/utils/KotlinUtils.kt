package fr.c1.chatbot.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * To bool
 *
 * @return
 */
fun Int.toBool(): Boolean = this != 0

/**
 * To int
 *
 * @return
 */
fun Boolean.toInt(): Int = if (this) 1 else 0

/**
 * To float
 *
 * @return
 */
fun Boolean.toFloat(): Float = if (this) 1f else 0f

/**
 * To date
 *
 * @return
 */
fun Long.toDate(): String =
    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(this))
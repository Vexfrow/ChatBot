package fr.c1.chatbot.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** 0 -> false, * -> true */
fun Int.toBool(): Boolean = this != 0

/** true -> 1, false -> 0 */
fun Boolean.toInt(): Int = if (this) 1 else 0

/** @see toInt */
fun Boolean.toFloat(): Float = if (this) 1f else 0f

/** To date */
fun Long.toDate(): String =
    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(this))

/** Get [count] items randomly in the list */
fun <T> Collection<T>.randoms(count: Int): List<T> = List(count) { random() }
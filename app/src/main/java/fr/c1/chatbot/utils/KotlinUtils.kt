package fr.c1.chatbot.utils

fun Int.toBool(): Boolean = this != 0
fun Boolean.toInt(): Int = if (this) 1 else 0
fun Boolean.toFloat(): Float = if (this) 1f else 0f
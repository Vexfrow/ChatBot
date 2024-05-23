package fr.c1.chatbot.utils

import androidx.core.content.ContextCompat
import android.content.Context
import android.content.pm.PackageManager

fun Context.hasPermission(permission: String) = ContextCompat.checkSelfPermission(
    this,
    permission
) == PackageManager.PERMISSION_GRANTED
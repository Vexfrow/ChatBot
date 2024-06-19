package fr.c1.chatbot.model

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import fr.c1.chatbot.ui.icons.Bot
import fr.c1.chatbot.ui.icons.Robot
import fr.c1.chatbot.ui.icons.RobotFace
import fr.c1.chatbot.utils.getBool
import fr.c1.chatbot.utils.getColor
import fr.c1.chatbot.utils.getNullable
import fr.c1.chatbot.utils.getSp
import fr.c1.chatbot.utils.getString
import fr.c1.chatbot.utils.getUri
import fr.c1.chatbot.utils.putBool
import fr.c1.chatbot.utils.putColor
import fr.c1.chatbot.utils.putSp
import fr.c1.chatbot.utils.putString
import fr.c1.chatbot.utils.saveImage

private const val TAG = "Settings"

object Settings {

    private var defaultBubbleSpeechColor = Color.Blue
    private var defaultBackgroundColor = Color(136, 227, 160)
    private var defaultBotName = "Rob"
    private var defaultBotPersonality = "Rob"

    var textSize: TextUnit by mutableStateOf(40.sp)
    var tts: Boolean by mutableStateOf(false)
    var botIcon: ImageVector by mutableStateOf(Icons.Default.Bot)
    var botImage: Uri? by mutableStateOf(null)
    var userIcon: ImageVector by mutableStateOf(Icons.Default.Person)
    var userImage: Uri? by mutableStateOf(null)
    var bubbleSpeechBotColor: Color by mutableStateOf(defaultBubbleSpeechColor)
    var bubbleSpeechUserColor: Color by mutableStateOf(defaultBubbleSpeechColor)
    var backgroundColor: Color by mutableStateOf(defaultBackgroundColor)
    var botName: String by mutableStateOf(defaultBotName)
    var botPersonality: String by mutableStateOf(defaultBotPersonality)
    var notifications: Boolean by mutableStateOf(true)

    val iconsAvailable = with(Icons.Default) {
        listOf(
            Bot, Robot, RobotFace, Person, AccountCircle, AccountBox
        )
    }


    private fun iconFromName(name: String) = iconsAvailable.first { it.name == name }

    fun init(ctx: Context) {
        val pref = ctx.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        if (pref.contains("init")) from(pref)
        else reset()
    }

    private fun reset() {
        textSize = 40.sp
        tts = false
        botIcon = Icons.Default.Bot
        botImage = null
        userIcon = Icons.Default.Person
        userImage = null
        bubbleSpeechBotColor = defaultBubbleSpeechColor
        bubbleSpeechUserColor = defaultBubbleSpeechColor
        backgroundColor = defaultBackgroundColor
        botName = defaultBotName
        botPersonality = defaultBotPersonality
        notifications = true

    }

    private fun from(pref: SharedPreferences) = pref.run {
        getSp(::textSize, 40.sp)
        getBool(::tts, false)
        botIcon = iconFromName(getString(::botIcon.name, "Filled.Bot")!!)
        getNullable(
            ::botImage, ::getUri, "https://upload.wikimedia.org/wikipedia/commons/4/41/Noimage.svg"
        )
        userIcon = iconFromName(getString(::userIcon.name, "Filled.Person")!!)
        getNullable(
            ::userImage, ::getUri, "https://upload.wikimedia.org/wikipedia/commons/4/41/Noimage.svg"
        )
        getColor(::bubbleSpeechBotColor, defaultBubbleSpeechColor.value.toInt())
        getColor(::bubbleSpeechUserColor, defaultBubbleSpeechColor.value.toInt())
        getColor(::backgroundColor, defaultBackgroundColor.value.toInt())
        getString(::botName, defaultBotName)
        getString(::botPersonality, defaultBotPersonality)
        getBool(::notifications, false)

        Log.i(TAG, "Settings loaded: ${this@Settings}")
    }

    fun save(context: Context) =
        context.getSharedPreferences("Settings", Context.MODE_PRIVATE).edit(true) {
            putBoolean("init", true)

            putSp(::textSize)
            putBool(::tts)
            putString(::botIcon.name, botIcon.name)
            saveImage(::botImage, context)
            putString(::userIcon.name, userIcon.name)
            saveImage(::userImage, context)
            putColor(::bubbleSpeechBotColor)
            putColor(::bubbleSpeechUserColor)
            putColor(::backgroundColor)
            putString(::botName)
            putString(::botPersonality)
            putBool(::notifications)

            Log.i(TAG, "Settings saved : ${this@Settings}")
        }

    override fun toString(): String {
        return "Settings(textSize=$textSize, stt=$tts, botIcon=$botIcon, botImage=$botImage, userIcon=$userIcon, userImage=$userImage)"
    }
}
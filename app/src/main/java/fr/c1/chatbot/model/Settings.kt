package fr.c1.chatbot.model

import fr.c1.chatbot.ui.icons.Bot
import fr.c1.chatbot.ui.icons.Robot
import fr.c1.chatbot.ui.icons.RobotFace
import fr.c1.chatbot.ui.theme.getColorList
import fr.c1.chatbot.utils.foreground
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
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.Uri
import android.util.Log

private const val TAG = "Settings"

object Settings {
    private object Default {
        val defaultBubbleSpeechColor = Color.Blue
        val defaultBackgroundColor = Color.Unspecified
        const val defaultBotName = "Rob"
        const val defaultBotPersonality = "Rob"
    }

    var textSize: TextUnit by mutableStateOf(40.sp)
    var tts: Boolean by mutableStateOf(false)
    var botIcon: ImageVector by mutableStateOf(Icons.Default.Bot)
    var botImage: Uri? by mutableStateOf(null)
    var userIcon: ImageVector by mutableStateOf(Icons.Default.Person)
    var userImage: Uri? by mutableStateOf(null)
    var bubbleSpeechBotColor: Color by mutableStateOf(Default.defaultBubbleSpeechColor)
    val textBotColor: Color get() = bubbleSpeechBotColor.foreground
    var bubbleSpeechUserColor: Color by mutableStateOf(Default.defaultBubbleSpeechColor)
    val textUserColor: Color get() = bubbleSpeechUserColor.foreground
    var backgroundColor: Color by mutableStateOf(Default.defaultBackgroundColor)
    val foregroundColor: Color get() = backgroundColor.foreground
    var botName: String by mutableStateOf(Default.defaultBotName)
    var botPersonality: String by mutableStateOf(Default.defaultBotPersonality)
    var notifications: Boolean by mutableStateOf(true)

    val iconsAvailable = with(Icons.Default) {
        listOf(
            Bot, Robot, RobotFace, Person, AccountCircle, AccountBox
        )
    }


    private fun iconFromName(name: String) = iconsAvailable.first { it.name == name }

    fun init(ctx: Context) {
        val pref = ctx.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val night = ctx.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        if (pref.contains("init")) from(pref, night)
        else reset(night)
    }

    private fun reset(night: Boolean) {
        textSize = 40.sp
        tts = false
        botIcon = Icons.Default.Bot
        botImage = null
        userIcon = Icons.Default.Person
        userImage = null
        getColorList(!night).let {
            bubbleSpeechBotColor = it[0]
            bubbleSpeechUserColor = it[1]
        }
        backgroundColor = Default.defaultBackgroundColor
        botName = Default.defaultBotName
        botPersonality = Default.defaultBotPersonality
        notifications = true

    }

    private fun from(pref: SharedPreferences, night: Boolean) = pref.run {
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
        val tmp = getColorList(night)

        getColor(::bubbleSpeechBotColor, tmp[0])
        getColor(::bubbleSpeechUserColor, tmp[1])
        getColor(::backgroundColor, Default.defaultBackgroundColor)
        getString(::botName, Default.defaultBotName)
        getString(::botPersonality, Default.defaultBotPersonality)
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
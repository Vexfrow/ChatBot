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
import fr.c1.chatbot.ui.theme.LocalColorSchemeExtension
import fr.c1.chatbot.utils.getBool
import fr.c1.chatbot.utils.getColor
import fr.c1.chatbot.utils.getNullable
import fr.c1.chatbot.utils.getSp
import fr.c1.chatbot.utils.getUri
import fr.c1.chatbot.utils.putBool
import fr.c1.chatbot.utils.putColor
import fr.c1.chatbot.utils.putSp
import fr.c1.chatbot.utils.saveImage

private const val TAG = "Settings"

object Settings {
    var textSize: TextUnit by mutableStateOf(40.sp)
    var tts: Boolean by mutableStateOf(false)
    var botIcon: ImageVector by mutableStateOf(Icons.Default.Bot)
    var botImage: Uri? by mutableStateOf(null)
    var userIcon: ImageVector by mutableStateOf(Icons.Default.Person)
    var userImage: Uri? by mutableStateOf(null)
    var bubbleSpeachBotColor : Color by mutableStateOf(Color.Blue)
    var bubbleSpeachUserColor : Color by mutableStateOf(Color.Blue)
    var fontColor : Color by mutableStateOf(Color.White)

    val iconsAvailable = with(Icons.Default) {
        listOf(
            Bot,
            Robot,
            RobotFace,
            Person,
            AccountCircle,
            AccountBox
        )
    }


    fun iconFromName(name: String) = iconsAvailable.first { it.name == name }

    fun init(ctx: Context) {
        val pref = ctx.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        if (pref.contains("init"))
            from(pref)
        else
            reset(ctx)
    }

    fun reset(context: Context) {
        textSize = 40.sp
        tts = false
        botIcon = Icons.Default.Bot
        botImage = null
        userIcon = Icons.Default.Person
        userImage = null
        bubbleSpeachBotColor = Color.Blue
        bubbleSpeachUserColor = Color.Blue
        fontColor = Color.White
    }

    private fun from(pref: SharedPreferences) = pref.run {
        getSp(::textSize, 40.sp)
        getBool(::tts, false)
        botIcon = iconFromName(getString(::botIcon.name, "Filled.Bot")!!)
        getNullable(
            ::botImage,
            ::getUri,
            "https://upload.wikimedia.org/wikipedia/commons/4/41/Noimage.svg"
        )
        userIcon = iconFromName(getString(::userIcon.name, "Filled.Person")!!)
        getNullable(
            ::userImage,
            ::getUri,
            "https://upload.wikimedia.org/wikipedia/commons/4/41/Noimage.svg"
        )
        getColor(::bubbleSpeachBotColor, Color.Blue.value.toInt())
        getColor(::bubbleSpeachUserColor, Color.Blue.value.toInt())
        getColor(::fontColor, Color.Blue.value.toInt())

        Log.i(TAG, "Settings loaded: ${this@Settings}")
    }

    fun save(context: Context) = context
        .getSharedPreferences("Settings", Context.MODE_PRIVATE)
        .edit(true) {
            putBoolean("init", true)

            putSp(::textSize)
            putBool(::tts)
            putString(::botIcon.name, botIcon.name)
            saveImage(::botImage, context)
            putString(::userIcon.name, userIcon.name)
            saveImage(::userImage, context)
            putColor(::bubbleSpeachBotColor)
            putColor(::bubbleSpeachUserColor)
            putColor(::fontColor)

            Log.i(TAG, "Settings saved : ${this@Settings}")
        }

    override fun toString(): String {
        return "Settings(textSize=$textSize, stt=$tts, botIcon=$botIcon, botImage=$botImage, userIcon=$userIcon, userImage=$userImage)"
    }
}
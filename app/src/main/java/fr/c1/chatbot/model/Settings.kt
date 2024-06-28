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

/** Object containing the settings of the app */
object Settings {
    /** Object containing the default values of the [Settings] */
    private object Default {
        /** @see bubbleSpeechUserColor */
        val defaultBubbleSpeechColor = Color.Blue

        /** @see backgroundColor */
        val defaultBackgroundColor = Color.Unspecified

        /** @see botName */
        const val defaultBotName = "Rob"

        /** @see botPersonality */
        const val defaultBotPersonality = "Rob"
    }

    /** Size of the text, 40 sp by default */
    var textSize: TextUnit by mutableStateOf(40.sp)

    /** Indicate if the tts is enabled, disabled by default */
    var tts: Boolean by mutableStateOf(false)

    /** Icon of the bot, [Icons.Filled.Bot] by default */
    var botIcon: ImageVector by mutableStateOf(Icons.Default.Bot)

    /** Optional image of the bot, null by default */
    var botImage: Uri? by mutableStateOf(null)

    /** Icon of the user, [Icons.Filled.Person] by default */
    var userIcon: ImageVector by mutableStateOf(Icons.Default.Person)

    /** Optional image of the user, null by default */
    var userImage: Uri? by mutableStateOf(null)

    /** Color of the bot speech bubble, [Default.defaultBubbleSpeechColor] by default */
    var bubbleSpeechBotColor: Color by mutableStateOf(Default.defaultBubbleSpeechColor)

    /** Get the color of the text on the [bubbleSpeechBotColor] */
    val textBotColor: Color get() = bubbleSpeechBotColor.foreground

    /** Color of the user speech bubble, [Default.defaultBubbleSpeechColor] by default */
    var bubbleSpeechUserColor: Color by mutableStateOf(Default.defaultBubbleSpeechColor)

    /** Get the color of the text on the [bubbleSpeechUserColor] */
    val textUserColor: Color get() = bubbleSpeechUserColor.foreground

    /** Color of the app background, [Default.defaultBackgroundColor] by default */
    var backgroundColor: Color by mutableStateOf(Default.defaultBackgroundColor)

    /** Get the color of the text on the [backgroundColor] */
    val foregroundColor: Color get() = backgroundColor.foreground
    /** Name of the bot, [Default.defaultBotName] by default */
    var botName: String by mutableStateOf(Default.defaultBotName)
    /** Personality of the bot, [Default.defaultBotPersonality] by default */
    var botPersonality: String by mutableStateOf(Default.defaultBotPersonality)
    /** Indicate if the notifications are enabled, disabled by default */
    var notifications: Boolean by mutableStateOf(true)

    /** Icons avaible for the bot and the user */
    val iconsAvailable = with(Icons.Default) {
        listOf(
            Bot, Robot, RobotFace, Person, AccountCircle, AccountBox
        )
    }

    /**
     * Get the [iconsAvailable] from his [name]
     *
     * @param name [ImageVector.name]
     */
    private fun iconFromName(name: String) = iconsAvailable.first { it.name == name }

    /**
     * Initialize the settings from the [SharedPreferences] or create it
     *
     * @param ctx Current Android context
     * @see from Init from the [SharedPreferences]
     * @see reset Create the [SharedPreferences]
     */
    fun init(ctx: Context) {
        val pref = ctx.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val night =
            ctx.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        if (pref.contains("init")) from(pref, night)
        else reset(night)
    }

    /**
     * Reset the settings
     *
     * @param night Indicate if the dark mode is enabled ([Configuration.UI_MODE_NIGHT_YES])
     */
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

    /**
     * Get the settings from a [pref]
     *
     * @param night Indicate if the dark mode is enabled ([Configuration.UI_MODE_NIGHT_YES])
     */
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

    /**
     * Save the settings into a [SharedPreferences]
     *
     * @param context Current Android context
     *
     * @see SharedPreferences.Editor
     */
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
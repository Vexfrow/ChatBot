package fr.c1.chatbot

import fr.c1.chatbot.repositories.ActivitiesRepository
import fr.c1.chatbot.model.Settings
import fr.c1.chatbot.utils.TTS
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import android.app.Application

/** ChatBot TAG */
private const val TAG = "ChatBot"

/**
 * Application class. Contains all "global" variables
 *
 * @constructor Automatically called by android
 */
class ChatBot : Application() {
    /** Indicate if the Application is ready to operate */
    var inited by mutableStateOf(value = false)
        private set

    /** Activities repository to get the activities */
    val activitiesRepository: ActivitiesRepository = ActivitiesRepository()

    /** TTS system */
    lateinit var tts: TTS
        private set

    /**
     * Initialize the application :
     * - Init the [Settings]
     * - Init the [tts]
     *
     * @see Settings.init
     * @see TTS
     */
    fun init() {
        if (inited)
            return

        Settings.init(this)

        tts = TTS(this)

        inited = true
    }
}
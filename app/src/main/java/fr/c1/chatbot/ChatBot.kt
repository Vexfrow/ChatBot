package fr.c1.chatbot

import fr.c1.chatbot.model.ActivitiesRepository
import fr.c1.chatbot.model.Settings
import fr.c1.chatbot.model.Tree
import fr.c1.chatbot.utils.TTS
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import android.app.Application
import java.io.InputStream

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

    /** Tree that represent the bot questions and user answers */
    val chatbotTree = Tree()

    /** TTS system */
    lateinit var tts: TTS
        private set

    /**
     * Initialize the application :
     * - Init the [Settings]
     * - Init the [chatbotTree]
     * - Init the [tts]
     *
     * @see Settings.init
     * @see Tree.initTree
     * @see TTS
     */
    fun init() {
        if (inited)
            return

        Settings.init(this)

        val mapScript: Map<String, InputStream> = mapOf(
            "Rob" to resources.openRawResource(R.raw.rob),
            "Amy" to resources.openRawResource(R.raw.amy),
            "Georges" to resources.openRawResource(R.raw.georges)
        )

        chatbotTree.initTree(mapScript)

        tts = TTS(this)

        inited = true
    }
}
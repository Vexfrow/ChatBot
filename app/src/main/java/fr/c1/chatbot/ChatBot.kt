package fr.c1.chatbot

import fr.c1.chatbot.model.ActivitiesRepository
import fr.c1.chatbot.model.Settings
import fr.c1.chatbot.model.User
import fr.c1.chatbot.model.loadAllUsersInformation
import fr.c1.chatbot.model.storeAllUsersInformation
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
    var userList = mutableListOf<User>()

    /** The current user currently "connected" */
    lateinit var currentUser: User

    /** TTS system */
    lateinit var tts: TTS
        private set

    /**
     * Initialize the application :
     * - Init the [Settings]
     * - Init the [chatbotTree]
     * - Init the [tts]
     * - Init the [userList]
     *
     * @see Settings.init
     * @see Tree.initTree
     * @see TTS
     * @see loadAllUsersInformation
     */
    fun init() {
        if (inited)
            return

        Settings.init(this)

        tts = TTS(this)


        // Load user list
        userList = loadAllUsersInformation(this)
        if (userList.isEmpty()) {
            userList.add(User("1", "User", 20))
            storeAllUsersInformation(this, userList)
        }
        currentUser = userList[0]

        inited = true
    }
}
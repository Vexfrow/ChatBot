package fr.c1.chatbot

import fr.c1.chatbot.model.ActivitiesRepository
import fr.c1.chatbot.model.Settings
import fr.c1.chatbot.model.Tree
import fr.c1.chatbot.model.User
import fr.c1.chatbot.model.loadAllUsersInformation
import fr.c1.chatbot.model.storeAllUsersInformation
import fr.c1.chatbot.utils.TTS
import android.app.Application
import java.io.InputStream

private const val TAG = "ChatBot"

class ChatBot : Application() {
    val activitiesRepository: ActivitiesRepository = ActivitiesRepository()
    val chatbotTree = Tree()
    var userList = mutableListOf<User>()
    lateinit var currentUser: User

    lateinit var tts: TTS
        private set

    override fun onCreate() {
        super.onCreate()
        Settings.init(this)

        val mapScript : Map<String, InputStream> = mapOf("Rob" to resources.openRawResource(R.raw.rob), "Amy" to resources.openRawResource(R.raw.amy), "Georges" to resources.openRawResource(R.raw.georges))

        chatbotTree.initTree(mapScript)

        tts = TTS(this)


        // Load user list
        userList = loadAllUsersInformation(this)
        if (userList.isEmpty()) {
            userList.add(User("1", "User", 20))
            storeAllUsersInformation(this, userList)
        }
        currentUser = userList[0]
    }
}
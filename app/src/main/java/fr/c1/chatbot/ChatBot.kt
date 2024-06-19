package fr.c1.chatbot

import fr.c1.chatbot.model.ActivitiesRepository
import fr.c1.chatbot.model.Settings
import fr.c1.chatbot.model.Tree
import fr.c1.chatbot.model.User
import fr.c1.chatbot.model.loadAllUsersInformation
import fr.c1.chatbot.model.storeAllUsersInformation
import fr.c1.chatbot.utils.TTS
import kotlinx.coroutines.delay
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import android.app.Application
import java.io.InputStream
import kotlin.time.Duration.Companion.seconds

private const val TAG = "ChatBot"

class ChatBot : Application() {
    var inited by mutableStateOf(value = false)
        private set

    val activitiesRepository: ActivitiesRepository = ActivitiesRepository()
    val chatbotTree = Tree()
    var userList = mutableListOf<User>()
    lateinit var currentUser: User

    lateinit var tts: TTS
        private set

    suspend fun init() {
        if (inited)
            return

        delay(20.seconds)
        Settings.init(this)

        val mapScript: Map<String, InputStream> = mapOf(
            "Rob" to resources.openRawResource(R.raw.rob),
            "Amy" to resources.openRawResource(R.raw.amy),
            "Georges" to resources.openRawResource(R.raw.georges)
        )

        chatbotTree.initTree(mapScript)

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
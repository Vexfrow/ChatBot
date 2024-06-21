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
import fr.c1.chatbot.viewModel.MessageVM

private const val TAG = "ChatBot"

class ChatBot : Application() {
    var inited by mutableStateOf(value = false)
        private set

    val activitiesRepository: ActivitiesRepository = ActivitiesRepository()
    var userList = mutableListOf<User>()
    lateinit var currentUser: User
    lateinit var tts: TTS
        private set

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
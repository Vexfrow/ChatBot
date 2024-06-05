package fr.c1.chatbot

import fr.c1.chatbot.model.ActivitiesRepository
import fr.c1.chatbot.model.Settings
import fr.c1.chatbot.model.Tree
import fr.c1.chatbot.utils.TTS
import android.app.Application
import fr.c1.chatbot.model.ProfilUtilisateur
import fr.c1.chatbot.model.loadAllUsersInformation

class ChatBot : Application() {
    val activitiesRepository: ActivitiesRepository = ActivitiesRepository()
    val chatbotTree = Tree()
    var userList = mutableListOf<ProfilUtilisateur>()
    lateinit var currentUser: ProfilUtilisateur

    lateinit var tts: TTS
        private set

    override fun onCreate() {
        super.onCreate()
        val fileIS = resources.openRawResource(R.raw.flow_chart)

        chatbotTree.initTree(fileIS)
        val activitiesRepository = ActivitiesRepository()
        activitiesRepository.initAll(this)

        tts = TTS(this)

        Settings.init(this)

        // Load user list
        userList = loadAllUsersInformation(this)
        currentUser = userList[0]
    }
}
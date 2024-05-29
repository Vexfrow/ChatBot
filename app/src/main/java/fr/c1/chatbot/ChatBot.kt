package fr.c1.chatbot

import fr.c1.chatbot.model.ActivitiesRepository
import fr.c1.chatbot.model.Settings
import fr.c1.chatbot.model.flowChart.Tree
import fr.c1.chatbot.utils.TTS
import android.app.Application

class ChatBot : Application() {
    val chatbotTree = Tree()

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
    }
}
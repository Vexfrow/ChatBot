package fr.c1.chatbot

import fr.c1.chatbot.model.ActivitiesRepository
import fr.c1.chatbot.model.Tree
import fr.c1.chatbot.utils.TTS
import android.app.Application

class ChatBot : Application() {
    lateinit var tts: TTS
        private set

    override fun onCreate() {
        super.onCreate()
        val chatbotTree = Tree()
        val fileIS = resources.openRawResource(R.raw.questionreponses)

        chatbotTree.initTree(fileIS)
        val activitiesRepository = ActivitiesRepository()
        activitiesRepository.initAll(this)

        tts = TTS(this)
    }
}
package fr.c1.chatbot

import android.app.Application
import fr.c1.chatbot.model.ActivitiesRepository
import fr.c1.chatbot.model.Tree

class ChatBot : Application() {
    override fun onCreate() {
        super.onCreate()
        val chatbotTree = Tree()
        val fileIS = resources.openRawResource(R.raw.questionreponses)

        chatbotTree.initTree(fileIS)
        val activitiesRepository = ActivitiesRepository()
        activitiesRepository.initAll(this)
    }

}
package fr.c1.chatbot

import android.app.Application
import fr.c1.chatbot.model.ActivitiesRepository

class ChatBot : Application() {
    override fun onCreate() {
        super.onCreate()
        val activitiesRepository = ActivitiesRepository()
        activitiesRepository.initAll(this)
        // Afficher la liste des musées
        activitiesRepository.displayAll()
    }

}
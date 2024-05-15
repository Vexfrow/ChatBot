package fr.c1.chatbot

import android.app.Application
import fr.c1.chatbot.model.ActivitiesRepository

class ChatBot : Application() {
    override fun onCreate() {
        super.onCreate()
        val activitiesRepository = ActivitiesRepository()
        activitiesRepository.initAll(this)
        // Afficher la liste des musées
        for (musee in activitiesRepository.getMuseesList()) {
            println(musee)
        }
        // Afficher la liste des sites patrimoniaux
        for (site in activitiesRepository.getSitesList()) {
            println(site)
        }
        // Afficher la liste des expositions
        for (exposition in activitiesRepository.getExpositionsList()) {
            println(exposition)
        }
        // Afficher la liste des contenus culturels
        for (contenu in activitiesRepository.getContenusList()) {
            println(contenu)
        }
        // Afficher la liste des édifices avec architecture remarquable
        for (edifice in activitiesRepository.getEdificesList()) {
            println(edifice)
        }
        // Afficher la liste des jardins remarquables
        for (jardin in activitiesRepository.getJardinsList()) {
            println(jardin)
        }
        // Afficher la liste des festivals
        for (festival in activitiesRepository.getFestivalsList()) {
            println(festival)
        }
    }

}
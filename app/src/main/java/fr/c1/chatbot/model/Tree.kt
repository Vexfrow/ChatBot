package fr.c1.chatbot.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.c1.chatbot.model.activity.Type.CULTURE
import fr.c1.chatbot.model.activity.Type.SPORT
<<<<<<< HEAD
=======
import android.util.Log
>>>>>>> 0c3a669 (Implemented actions :)
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Type

private const val TAG = "Tree"
const val retour = -1
const val recommencerConversation = -2

class Robot {
    var id: Int = 0
    var text: String = ""
    var action: String? = null // Optional field
}

class Humain {
    var id: Int = 0
    var text: String = ""
    var action: String? = null // Optional field
}

class Link {
    var from: Int = 0
    var to: Int = 0
    var answer: Int = 0
}


class Data {
    var robot: List<Robot> = ArrayList()
    var humain: List<Humain> = ArrayList()
    var link: List<Link> = ArrayList()
}

class Tree {

<<<<<<< HEAD
    var data: Data? = null
    private var historiqueQuestion: ArrayList<Int> = ArrayList()
=======
    private var currentQuestionId = 0
    var data: Data? = null
>>>>>>> 0c3a669 (Implemented actions :)

    //Prend un fichier JSON en paramètre
    fun initTree(fileIS: InputStream) {
        val gson = Gson()
        try {
            BufferedReader(InputStreamReader(fileIS)).use { bufferedReader ->
                val dataType: Type = object : TypeToken<Data?>() {}.type
                data = gson.fromJson(bufferedReader, dataType)
            }
            historiqueQuestion.add(0)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    //Renvoie le texte de la question posé par le bot
    fun getQuestion(): String {
        return data?.robot?.get(historiqueQuestion[historiqueQuestion.size - 1])?.text ?: ""
    }

    //Renvoie la liste des id des réponses possibles lié à la question actuelle
    fun getAnswersId(): ArrayList<Int> {
        val currentAnswers = ArrayList<Int>()
        if (historiqueQuestion.size != 1) {
            currentAnswers.add(recommencerConversation)
            currentAnswers.add(retour)
        }

        for (r in data?.link!!) {
            if (r.from == historiqueQuestion[historiqueQuestion.size - 1]) {
                currentAnswers.add(r.answer)
            }
        }
        return currentAnswers
    }


    //Lorsqu'on choisie une réponse, la question est maj et le nom de l'action est renvoyé
    fun selectAnswer(idReponse: Int, activitiesRepository: ActivitiesRepository) {
<<<<<<< HEAD
        if (idReponse == retour && historiqueQuestion.size > 1)
            historiqueQuestion.removeLast()
        else if (idReponse == recommencerConversation) { //Clear la conversation ?
            historiqueQuestion.removeAll(historiqueQuestion.toSet())
            historiqueQuestion.add(0)
        } else {
            for (r in data?.link!!) {
                if (r.from == historiqueQuestion[historiqueQuestion.size - 1] && r.answer == idReponse) {
                    historiqueQuestion.add(r.to)
                    // Afficher le texte de la réponse
                    Log.d(TAG, "selectAnswer: ${getAnswerText(idReponse)}")
                    // Remplir les éléments de ActivitiesRepository avec les données de la réponse
                    Log.d(TAG, "selectAnswer: ${getActionUtilisateur(idReponse)}")
                    when (getActionUtilisateur(idReponse)) {
                        "Geolocalisation" -> {
                            // TODO : Récupérer la localisation courante de l'utilisateur
                            //activitiesRepository.setLocalisation()
                        }

                        "ActivitePhysique" -> {
                            activitiesRepository.setType(SPORT)
                        }

                        "ActiviteCulturelle" -> {
                            activitiesRepository.setType(CULTURE)
                        }
                    }
                    Log.d(TAG, "selectAnswer: new type : ${activitiesRepository.getType()}")
=======
        for (r in data?.link!!) {
            if (r.from == currentQuestionId && r.answer == idReponse) {
                currentQuestionId = r.to
                // Afficher le texte de la réponse
                Log.d(TAG, "selectAnswer: ${getAnswerText(idReponse)}")
                // Remplir les éléments de ActivitiesRepository avec les données de la réponse
                Log.d(TAG, "selectAnswer: ${getActionUtilisateur(idReponse)}")
                when (getActionUtilisateur(idReponse)) {
                    TypeAction.Geolocalisation -> {
                        // TODO : Récupérer la localisation courante de l'utilisateur
                        //activitiesRepository.setLocalisation()
                    }

                    TypeAction.ActivitePhysique -> activitiesRepository.setType(SPORT)
                    TypeAction.ActiviteCulturelle -> activitiesRepository.setType(CULTURE)
                    else -> {}
>>>>>>> 0c3a669 (Implemented actions :)
                }
            }
        }
    }


    fun getAnswerText(idReponse: Int): String {
        for (h in data?.humain!!) {
            if (h.id == idReponse) {
                return h.text
            }
        }
        return ""
    }

<<<<<<< HEAD
    fun getActionUtilisateur(idReponse: Int): String? {

        for (h in data?.humain!!) {
            if (h.id == idReponse) {
                Log.d(TAG, "getActionUtilisateur: ${h.action}")
                return h.action
            }
        }
        return ""
    }

=======
    fun getActionUtilisateur(idReponse: Int): TypeAction {
        val actionStr = data?.humain!!.first { h -> h.id == idReponse }.action
        return if (actionStr == null) TypeAction.None else enumValueOf<TypeAction>(actionStr)
    }
>>>>>>> 0c3a669 (Implemented actions :)
}
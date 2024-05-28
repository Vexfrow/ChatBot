package fr.c1.chatbot.model.flowChart

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Type


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
    var robot: List<Robot>? = null
    var humain: List<Humain>? = null
    var link: List<Link>? = null
}

class Tree {

    private var currentQuestionId = 0
    var data : Data? = null

    //Prend un fichier JSON en paramètre
    fun initTree(fileIS: InputStream) {
        val gson = Gson()
        try {
                BufferedReader(InputStreamReader(fileIS)).use { bufferedReader ->
                val dataType: Type = object : TypeToken<Data?>() {}.type
                data = gson.fromJson(bufferedReader, dataType)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    //Renvoie le texte de la question posé par le bot
    fun getQuestion(): String {
        return data?.robot?.get(currentQuestionId)?.text ?: ""
    }

    //Renvoie la liste des id des réponses possibles lié à la question actuelle
    fun getAnswersId(): ArrayList<Int> {
        val currentAnswers = ArrayList<Int>()

        for (r in data?.link!!) {
            if (r.from == currentQuestionId) {
                currentAnswers.add(r.answer)
            }
        }
        return currentAnswers
    }


    //Lorsqu'on choisie une réponse, la question est maj et le nom de l'action est renvoyé
    fun selectAnswer(idReponse: Int) {
        for (r in data?.link!!) {
            if (r.from == currentQuestionId && r.answer == idReponse) {
                currentQuestionId = r.to
            }
        }
    }


    fun getAnswerText(idReponse: Int): String {
        for(h in data?.humain!!){
            if(h.id == idReponse){
                return h.text
            }
        }
        return ""
    }

    fun getActionUtilisateur(idReponse: Int): String? {
        for(h in data?.humain!!){
            if(h.id == idReponse){
                return h.action
            }
        }
        return ""
    }


}
package fr.c1.chatbot.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Type


class Question {
    var id: Int = 0
    var text: String = ""
    var action: String = "" // Optional field
}

class Reponse {
    var id: Int = 0
    var text: String = ""
    var action: String = "" // Optional field
}

class Link {
    var from: Int = 0
    var to: Int = 0
    var answer: Int = 0
}


class Data {
    var robot: List<Question> = ArrayList()
    var humain: List<Reponse> = ArrayList()
    var link: List<Link> = ArrayList()
}

class Tree {

    private var currentQuestionId = 0
    var data: Data = Data()

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
        return data.robot[currentQuestionId].text
    }


    //Lorsqu'on choisie une réponse, la question est maj et le nom de l'action est renvoyé
    fun selectAnswer(idReponse: Int) {
        for (r in data.link) {
            if (r.from == currentQuestionId && r.answer == idReponse) {
                currentQuestionId = r.to
            }
        }
    }


    //Récupère la liste des réponses
    fun getAnswers():ArrayList<Reponse> {
        val currentAnswers = ArrayList<Reponse>()
        for (q in data.link) {
            if (q.from == currentQuestionId)
                for(r in data.humain)
                    if (r.id == q.answer)
                        currentAnswers.add(r)
        }
        return currentAnswers
    }

    //Récupère un objet Action lié à une réponse/question
    fun getActionUtilisateur(reponse : Reponse): Action.TypeAction {
        return Action.stringToAction(reponse.action)
    }




}
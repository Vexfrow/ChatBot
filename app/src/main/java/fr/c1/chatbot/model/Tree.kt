package fr.c1.chatbot.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.util.Log
import fr.c1.chatbot.R
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Type

private const val TAG = "Tree"
const val retour = -1
const val recommencerConversation = -2
const val afficherFiltre = -3

class Robot {
    var id: Int = 0
    var text: String = ""
    var action: String? = null // Optional field
}

class Human {
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
    var humain: List<Human> = ArrayList()
    var link: List<Link> = ArrayList()
}

class Tree {

    private var dataList = mutableMapOf<String, Data>()

    private var questionsHistory: ArrayList<Int> = ArrayList()
    private var currentScript = "Rob"
    private var currentData: Data? = null

    //Take a json file in parameter
    fun initTree(mapScript: Map<String, InputStream>) {
        val gson = Gson()
        try {
            mapScript.entries
            mapScript.forEach { entry ->
                BufferedReader(InputStreamReader(entry.value)).use { bufferedReader ->
                    val dataType: Type = object : TypeToken<Data?>() {}.type
                    dataList[entry.key] = gson.fromJson(bufferedReader, dataType)
                }
            }

            questionsHistory.add(0)
            currentScript = Settings.botPersonality
            currentData = dataList[currentScript]
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    //Return the question ask by the bot
    val question: String
        get() {
            return currentData?.robot?.get(questionsHistory.last())?.text + "${currentScript}" ?: ""
            //return currentData?.robot?.get(questionsHistory.last())?.text ?: ""
        }

    //Return the list of all the answers possible
    val answersId: ArrayList<Int>
        get() {
            val currentAnswers = ArrayList<Int>()
            if (questionsHistory.size != 1) {
                currentAnswers.add(recommencerConversation)
                currentAnswers.add(retour)
            }

            for (r in currentData?.link!!) {
                if (r.from == questionsHistory.last()) {
                    currentAnswers.add(r.answer)
                }
            }
            currentAnswers.add(afficherFiltre)
            return currentAnswers
        }


    //Update the current question and execute the action link to the answer chosen
    fun selectAnswer(idAnswer: Int, user: User) {

        if (currentScript != Settings.botPersonality) {
            currentScript = Settings.botPersonality
            currentData = dataList[currentScript]
        }

        if (idAnswer == retour && questionsHistory.size > 1) questionsHistory.removeLast()
        else if (idAnswer == recommencerConversation) { //Clear previous messages ?
            questionsHistory.removeAll(questionsHistory.toSet())
            questionsHistory.add(0)
        } else if (idAnswer == afficherFiltre) {
            //val text = "Voici les filtres utilisés pour le moment : \nVilles : ${user.getVilles()}\nTypes d'activités : ${user.getTypes()}\n Distance préféré : ${getDistance()}\n Date voulue : ${app.getDate()}"
            //val saveText = getQuestion()

            Log.d(TAG, "selectAnswer: afficherFiltre")
        } else {
            for (r in currentData?.link!!) {
                if (r.from == questionsHistory.last() && r.answer == idAnswer) {
                    questionsHistory.add(r.to)
                    // Print the answer's text
                    Log.d(TAG, "selectAnswer: ${getAnswerText(idAnswer)}")
                    // Fill the ActivitiesRepository according to the answer selected
                    Log.d(TAG, "selectAnswer: ${getUserAction(idAnswer)}")

                }
            }
            Log.d(TAG, "selectAnswer: new type : ${user.types}")
        }
    }


    fun getAnswerText(idAnswer: Int): String {
        return currentData?.humain!!.first { h -> h.id == idAnswer }.text
    }

    fun getUserAction(idAnswer: Int): TypeAction {
        val actionStr = currentData?.humain!!.first { h -> h.id == idAnswer }.action
        return if (actionStr == null) TypeAction.None else enumValueOf<TypeAction>(actionStr)
    }

    val botAction: TypeAction
        get() {
            val actionStr =
                currentData?.robot!!.first { h -> h.id == questionsHistory.last() }.action
            return if (actionStr == null) TypeAction.None else enumValueOf<TypeAction>(actionStr)
        }
}
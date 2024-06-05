package fr.c1.chatbot.model

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.c1.chatbot.model.activity.Type.CULTURE
import fr.c1.chatbot.model.activity.Type.SPORT
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

    var data: Data? = null
    private var questionsHistory: ArrayList<Int> = ArrayList()

    //Take a json file in parameter
    fun initTree(fileIS: InputStream) {
        val gson = Gson()
        try {
            BufferedReader(InputStreamReader(fileIS)).use { bufferedReader ->
                val dataType: Type = object : TypeToken<Data?>() {}.type
                data = gson.fromJson(bufferedReader, dataType)
            }
            questionsHistory.add(0)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    //Return the question ask by the bot
    fun getQuestion(): String {
        return data?.robot?.get(questionsHistory.last())?.text ?: ""
    }

    //Return the list of all the answers possible
    fun getAnswersId(): ArrayList<Int> {
        val currentAnswers = ArrayList<Int>()
        if (questionsHistory.size != 1) {
            currentAnswers.add(recommencerConversation)
            currentAnswers.add(retour)
        }

        for (r in data?.link!!) {
            if (r.from == questionsHistory.last()) {
                currentAnswers.add(r.answer)
            }
        }
        currentAnswers.add(afficherFiltre)
        return currentAnswers
    }


    //Update the current question and execute the action link to the answer chosen
    fun selectAnswer(idAnswer: Int, activitiesRepository: ActivitiesRepository) {
        if (idAnswer == retour && questionsHistory.size > 1) questionsHistory.removeLast()
        else if (idAnswer == recommencerConversation) { //Clear previous messages ?
            questionsHistory.removeAll(questionsHistory.toSet())
            questionsHistory.add(0)
        } else if (idAnswer == afficherFiltre) {
            Log.d(TAG, "selectAnswer: afficherFiltre")
        } else {
            for (r in data?.link!!) {
                if (r.from == questionsHistory.last() && r.answer == idAnswer) {
                    questionsHistory.add(r.to)
                    // Print the answer's text
                    Log.d(TAG, "selectAnswer: ${getAnswerText(idAnswer)}")
                    // Fill the ActivitiesRepository according to the answer selected
                    Log.d(TAG, "selectAnswer: ${getUserAction(idAnswer)}")
                    when (getUserAction(idAnswer)) {
                        TypeAction.Geolocalisation -> {
                            // TODO : Take the current user's location
                            //activitiesRepository.setLocalisation()
                        }

                        TypeAction.ActivitePhysique -> activitiesRepository.setType(SPORT)
                        TypeAction.ActiviteCulturelle -> activitiesRepository.setType(CULTURE)
                        else -> {}
                    }
                    Log.d(TAG, "selectAnswer: new type : ${activitiesRepository.getType()}")
                }
            }
        }
    }


    fun getAnswerText(idAnswer: Int): String {
        return data?.humain!!.first { h -> h.id == idAnswer }.text
    }

    fun getUserAction(idAnswer: Int): TypeAction {
        val actionStr = data?.humain!!.first { h -> h.id == idAnswer }.action
        return if (actionStr == null) TypeAction.None else enumValueOf<TypeAction>(actionStr)
    }
}
package fr.c1.chatbot.model.messageManager

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.c1.chatbot.model.Settings
import fr.c1.chatbot.viewModel.MessageVM
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Type

private const val TAG = "Tree"
const val back = -1
const val restartConversation = -2
const val showFilter = -3

/**
 * Robot
 *
 * @constructor Create empty Robot
 */
class Robot {
    var id: Int = 0
    var text: String = ""
    var action: String? = null // Optional field
}

/**
 * Human
 *
 * @constructor Create empty Human
 */
class Human {
    var id: Int = 0
    var text: String = ""
    var action: String? = null // Optional field
}

/**
 * Link
 *
 * @constructor Create empty Link
 */
class Link {
    var from: Int = 0
    var to: Int = 0
    var answer: Int = 0
}


/**
 * Data
 *
 * @constructor Create empty Data
 */
class Data {
    var robot: List<Robot> = ArrayList()
    var humain: List<Human> = ArrayList()
    var link: List<Link> = ArrayList()
}

/**
 * Tree
 *
 * @constructor Create empty Tree
 */
class Tree {

    private var dataList = mutableMapOf<String, Data>()
    private var questionsHistory: ArrayList<Int> = ArrayList()
    private var currentScript = "Rob"
    private var currentData: Data? = null
    private var messageManager: MessageVM? = null

    /**
     * Init tree
     *
     * @param mvm : MessageVM : Object used for managing the messages
     * @param mapScript : Map<String, InputStream> : map of name and json file of the script
     */
    //Take a json file in parameter
    fun initTree(mvm: MessageVM, mapScript: Map<String, InputStream>) {
        messageManager = mvm
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


    /**
     * Return the question ask by the bot
     */
    val question: String
        get() {
            return currentData?.robot?.get(questionsHistory.last())?.text ?: ""
        }

    /**
     * Return the list of all the answers possible
     */
    val answersId: ArrayList<Int>
        get() {
            val currentAnswers = ArrayList<Int>()
            if (questionsHistory.size != 1) {
                currentAnswers.add(restartConversation)
                currentAnswers.add(back)
            }

            for (r in currentData?.link!!) {
                if (r.from == questionsHistory.last()) {
                    currentAnswers.add(r.answer)
                }
            }
            currentAnswers.add(showFilter)
            return currentAnswers
        }

    fun restart() {
        questionsHistory.removeAll(questionsHistory.toSet())
        questionsHistory.add(0)
    }

    fun back() {
        if (questionsHistory.size > 1) {
            questionsHistory.removeLast()
        }
    }


    /**
     * Select answer, update the current question and execute the action link to the answer chosen
     *
     * @param idAnswer
     */
    fun selectAnswer(idAnswer: Int) {

        if (currentScript != Settings.botPersonality) {
            currentScript = Settings.botPersonality
            currentData = dataList[currentScript]
        }

        for (r in currentData?.link!!) {
            if (r.from == questionsHistory.last() && r.answer == idAnswer) {
                questionsHistory.add(r.to)
                // Print the answer's text
                Log.d(TAG, "selectAnswer: ${getAnswerText(idAnswer)}")
                // Fill the ActivitiesRepository according to the answer selected
                Log.d(TAG, "selectAnswer: ${getUserAction(idAnswer)}")

            }
        }
    }


    /**
     * Get answer text
     *
     * @param idAnswer
     * @return String
     */
    fun getAnswerText(idAnswer: Int): String {
        return currentData?.humain!!.first { h -> h.id == idAnswer }.text
    }

    /**
     * Get user action
     *
     * @param idAnswer
     * @return TypeAction
     */
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
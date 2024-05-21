package fr.c1.chatbot.model

import android.util.Log
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader

class Tree {
    var currentAnswer = Answers("","", arrayListOf<Answers>());
    fun automate(index: Int){
        val ca = currentAnswer.getAnswers(index)

    }
    //Prend un fichier JSON en paramètre
    fun initTree(fileIS: InputStream){
        val bufferedReader = BufferedReader(InputStreamReader(fileIS))
        val jsonString =bufferedReader.readText()
        val currentAnswer = Gson().fromJson(jsonString, Answers::class.java)

        println(currentAnswer.answers);
    }
    fun getAnswersLabels() : ArrayList<String>{
        val labels = arrayListOf<String>()
        for(a in currentAnswer.answers){
            labels.add(a.label)
        }
        return labels
    }
    fun selectAnswer(index : Int) {
        currentAnswer = currentAnswer.getAnswers(index)
    }

    fun saveAnswer(){

    }
}
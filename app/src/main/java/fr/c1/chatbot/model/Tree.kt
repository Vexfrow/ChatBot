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

    fun saveAnswer(){

    }
}
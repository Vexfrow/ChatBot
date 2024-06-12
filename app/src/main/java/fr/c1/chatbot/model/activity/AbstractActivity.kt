package fr.c1.chatbot.model.activity

abstract class AbstractActivity(com: String) {
    var commune: String = com
    val passions = mutableListOf<String>()
}
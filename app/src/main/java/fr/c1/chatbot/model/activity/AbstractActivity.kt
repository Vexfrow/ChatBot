package fr.c1.chatbot.model.activity

abstract class AbstractActivity(com: String, lat: Double, long: Double) {
    var commune: String = com
    val passions = mutableListOf<String>()
    var latitude: Double = lat
    var longitude: Double = long
}
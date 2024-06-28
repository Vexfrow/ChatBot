package fr.c1.chatbot.model.activity

/**
 * Abstract class representing an activity.
 * @param com the commune where the activity is located
 * @param lat the latitude of the activity
 * @param long the longitude of the activity
 */
abstract class AbstractActivity(com: String, lat: Double, long: Double) {
    var commune: String = com
    val passions = mutableListOf<String>()
    var latitude: Double = lat
    var longitude: Double = long
}
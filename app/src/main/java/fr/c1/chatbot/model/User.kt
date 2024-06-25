package fr.c1.chatbot.model

import com.google.gson.Gson
import com.google.gson.JsonObject
import fr.c1.chatbot.model.activity.Type
import java.util.UUID

private const val TAG = "UserProfile"

/**
 * Represents a user
 * @property lastName Name of the user
 * @property firstName First name of the user
 * @property age Age of the user
 * @property _cities List of cities of the user
 * @property _types List of activity types of the user
 * @property _passions List of interests of the user
 * @property _weeklyPreferences List of weekly preferences of the user
 * @constructor Creates a user with the specified parameters
 */
class User private constructor(
    val id: String,
    var lastName: String,
    var firstName: String,
    var age: Int,
    private val _cities: MutableList<String> = mutableListOf(),
    private var _types: MutableList<Type> = mutableListOf(),
    private val _passions: MutableList<String> = mutableListOf(),
    private val _weeklyPreferences: MutableList<WeeklyPreference> = mutableListOf(),
) {
    companion object {
        fun from(id: String, obj: JsonObject): User {
            val gson = Gson()
            return User(
                id = id,
                lastName = obj[User::lastName.name].asString,
                firstName = obj[User::firstName.name].asString,
                age = obj[User::age.name].asInt,
                _cities = obj["cities"].asJsonArray.map { it.asString }.toMutableList(),
                _types = obj["types"].asJsonArray.map { Type.valueOf(it.asString) }.toMutableList(),
                _passions = obj["passions"].asJsonArray.map { it.asString }.toMutableList(),
                _weeklyPreferences = obj["weeklyPreferences"].asJsonArray.map {
                    gson.fromJson(it, WeeklyPreference::class.java)
                }.toMutableList()
            )
        }
    }

    constructor() : this(UUID.randomUUID().toString(), "user", "Default", 60)

    /**
     * Add a city
     */
    fun addCity(city: String) {
        _cities.add(city)
    }

    /**
     * Add an activity type
     */
    fun addType(type: Type) {
        _types.add(type)
    }

    /**
     * Add an interest
     */
    fun addPassion(passion: String) {
        _passions.add(passion)
    }

    /**
     * Add a weekly preference
     */
    fun addWeeklyPreference(day: String, houe: String, duration: Int) {
        _weeklyPreferences.add(WeeklyPreference(day, houe, duration))
    }

    /**
     * Delete a weekly preference
     */
    fun removeWeeklyPeference(day: String, hour: String, duration: Int) {
        _weeklyPreferences.remove(WeeklyPreference(day, hour, duration))
    }

    /**
     * Delete all weekly preferences
     */
    fun clearWeeklyPreferences() {
        _weeklyPreferences.clear()
    }

    /**
     * Delete a city
     */
    fun removeCity(city: String) {
        _cities.remove(city)
    }

    /**
     * Delete all cities
     */
    fun clearCities() {
        _cities.clear()
    }

    /**
     * Delete an interest
     */
    fun removePassion(passion: String) {
        _passions.remove(passion)
    }

    /**
     * Delete all interests
     */
    fun clearPassions() {
        _passions.clear()
    }

    /**
     * Delete an activity type
     */
    fun removeType(type: Type) {
        _types.remove(type)
    }

    /**
     * Delete all activity types
     */
    fun clearTypes() {
        _types.clear()
    }

    /**
     * Get the list of cities
     */
    val cities: List<String> get() = _cities

    /**
     * Get the list of activity types
     */
    val types: List<Type> get() = _types

    fun hasPassion(passion: String): Boolean = passions.contains(passion)

    /**
     * Get the list of interests
     */
    val passions: List<String>
        get() = _passions

    /**
     * Get the list of weekly preferences
     */
    val weeklyPreferences: List<WeeklyPreference> get() = _weeklyPreferences

    override fun toString(): String {
        return "User(id='$id', lastName='$lastName', firstName='$firstName', age=$age, _cities=$_cities, _types=$_types, _passions=$_passions, _weeklyPreferences=$_weeklyPreferences)"
    }
}

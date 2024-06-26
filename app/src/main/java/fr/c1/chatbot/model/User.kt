package fr.c1.chatbot.model

import com.google.gson.Gson
import com.google.gson.JsonObject
import fr.c1.chatbot.model.activity.Type
import java.util.UUID

private const val TAG = "UserProfile"

/**
 * User
 *
 * @property lastName
 * @property firstName
 * @property age
 * @property _cities
 * @property _types
 * @property _passions
 * @property _weeklyPreferences
 * @constructor Create User
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

        val DEFAULT = User(
            id = UUID(0x123456789ABCDEF, 0xFEDCBA987654321).toString(),
            firstName = "Default",
            lastName = "user",
            age = 60
        )
    }

    constructor() : this(UUID.randomUUID().toString(), "", "", 60)

    /**
     * Add city
     * @param city
     */
    fun addCity(city: String) {
        _cities.add(city)
    }

    /**
     * Add type
     * @param type
     */
    fun addType(type: Type) {
        _types.add(type)
    }

    /**
     * Add passion
     * @param passion
     */
    fun addPassion(passion: String) {
        _passions.add(passion)
    }

    /**
     * Add weekly preference
     * @param day
     * @param houe
     * @param duration
     */
    fun addWeeklyPreference(day: String, houe: String, duration: Int) {
        _weeklyPreferences.add(WeeklyPreference(day, houe, duration))
    }

    /**
     * Remove weekly peference
     * @param day
     * @param hour
     * @param duration
     */
    fun removeWeeklyPeference(day: String, hour: String, duration: Int) {
        _weeklyPreferences.remove(WeeklyPreference(day, hour, duration))
    }

    /**
     * Clear weekly preferences
     */
    fun clearWeeklyPreferences() {
        _weeklyPreferences.clear()
    }

    /**
     * Remove city
     * @param city
     */
    fun removeCity(city: String) {
        _cities.remove(city)
    }

    /**
     * Clear cities
     */
    fun clearCities() {
        _cities.clear()
    }

    /**
     * Remove passion
     * @param passion
     */
    fun removePassion(passion: String) {
        _passions.remove(passion)
    }

    /**
     * Clear passions
     */
    fun clearPassions() {
        _passions.clear()
    }

    /**
     * Remove type
     * @param type
     */
    fun removeType(type: Type) {
        _types.remove(type)
    }

    /**
     * Clear types
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

    /**
     * Has passion
     *
     * @param passion
     * @return Boolean true if the user has the passion, false otherwise
     */
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

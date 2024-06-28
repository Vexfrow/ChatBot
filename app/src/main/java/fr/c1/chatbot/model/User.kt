package fr.c1.chatbot.model

import com.google.gson.Gson
import com.google.gson.JsonObject
import fr.c1.chatbot.model.activity.Type
import java.util.UUID

private const val TAG = "UserProfile"

/**
 * Represents a user
 * @property id Id of the user
 * @property lastName Name of the user
 * @property firstName First name of the user
 * @property age Age of the user
 * @property _cities List of cities of the user
 * @property _types List of activity types of the user
 * @property _passions List of interests of the user
 * @property _weeklyPreferences List of weekly preferences of the user
 * @constructor Private: creates a user with all the specified parameters
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
        /**
         * Parse a [JsonObject] into a [User]
         *
         * @param id Id of the parsed user
         * @param obj [JsonObject] representing the user [id]
         * @return The parsed user
         */
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

        /**
         * The default user
         *
         * - [id] = "01234567-89ab-cdef-0fed-cba987654321"
         * - [firstName] = "Utilisateur"
         * - [lastName] = "par défaut"
         * - [age] = 60
         */
        val DEFAULT = User(
            id = UUID(0x123456789ABCDEF, 0xFEDCBA987654321).toString(),
            firstName = "Utilisateur",
            lastName = "par défaut",
            age = 60
        )
    }

    /**
     * Create a new user
     *
     * - [id] automatically generated with a [UUID.randomUUID]
     * - empty [firstName] and [lastName]
     * - [age] of 60
     */
    constructor() : this(UUID.randomUUID().toString(), "", "", 60)

    /** Add [city] to the [cities] */
    fun addCity(city: String) {
        _cities.add(city)
    }

    /** Add an activity [type] to the [types] */
    fun addType(type: Type) {
        _types.add(type)
    }

    /** Add a [passion] to the [passions] */
    fun addPassion(passion: String) {
        _passions.add(passion)
    }

    /**
     * Create a new [WeeklyPreference] into the [weeklyPreferences]
     *
     * @param day Day of the preference
     * @param houe Hour of the preference. ToDo: rename into hour
     * @param duration Duration of the preference
     */
    fun addWeeklyPreference(day: String, houe: String, duration: Int) {
        _weeklyPreferences.add(WeeklyPreference(day, houe, duration))
    }

    /**
     * Remove weekly peference
     *
     * ToDo: This way doesn't work!
     * Remove by predicte or override the [WeeklyPreference.toString] method
     *
     * @param day Day of the preference
     * @param hour Hour of the preference
     * @param duration Duration of the preference
     */
    fun removeWeeklyPeference(day: String, hour: String, duration: Int) {
        _weeklyPreferences.remove(WeeklyPreference(day, hour, duration))
    }

    /** Delete all [weeklyPreferences] */
    fun clearWeeklyPreferences() {
        _weeklyPreferences.clear()
    }

    /** Delete a [city] */
    fun removeCity(city: String) {
        _cities.remove(city)
    }

    /** Delete all [cities] */
    fun clearCities() {
        _cities.clear()
    }

    /** Delete a [passion] */
    fun removePassion(passion: String) {
        _passions.remove(passion)
    }

    /** * Clear [passions] */
    fun clearPassions() {
        _passions.clear()
    }

    /** Remove an activity [type] */
    fun removeType(type: Type) {
        _types.remove(type)
    }

    /** Delete all [types] */
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
     * Check if the [User] have a specified [passion]
     *
     * @return True if the user has the [passion], false otherwise
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

    override fun equals(other: Any?): Boolean = this === other || (other as? User)?.let {
        id == it.id &&
                lastName == it.lastName &&
                firstName == it.firstName &&
                age == it.age &&
                _cities == it._cities &&
                _types == it._types &&
                _passions == it._passions &&
                _weeklyPreferences == it._weeklyPreferences
    } ?: false

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + firstName.hashCode()
        result = 31 * result + age
        result = 31 * result + _cities.hashCode()
        result = 31 * result + _types.hashCode()
        result = 31 * result + _passions.hashCode()
        result = 31 * result + _weeklyPreferences.hashCode()
        return result
    }
}

package fr.c1.chatbot.model

import com.google.gson.Gson
import fr.c1.chatbot.model.activity.Type
import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileFilter

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
class User(
    var lastName: String = "",
    var firstName: String = "",
    var age: Int = -1,
    private val _cities: MutableList<String> = mutableListOf(),
    private var _types: MutableList<Type> = mutableListOf(),
    private val _passions: MutableList<String> = mutableListOf(),
    private var _weeklyPreferences: MutableList<WeeklyPreference> = mutableListOf()
) {

    /**
     * 2nd constructor
     */
    constructor(lastName: String, firstName: String, age: Int) : this() {
        this.lastName = lastName
        this.firstName = firstName
        this.age = age
    }

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

    /**
     * Store user information
     *
     * @param context
     */
    fun storeUserInformation(context: Context) {
        val fileName = "${lastName.lowercase()}_${firstName.lowercase()}.json"

        // Build the JSON content
        val jsonContent = """
        {
            "nom": "$lastName",
            "prenom": "$firstName",
            "age": $age,
            "villes": ${_cities.joinToString(prefix = "[", postfix = "]") { "\"$it\"" }},
            "types": ${_types.joinToString(prefix = "[", postfix = "]") { "\"$it\"" }},
            "passions": ${passions.joinToString(prefix = "[", postfix = "]") { "\"$it\"" }},
            "preferencesHebdo": ${
            weeklyPreferences.joinToString(
                prefix = "[",
                postfix = "]"
            ) { "\"$it\"" }
        }
        }
    """.trimIndent()

        // Write the JSON content to the file
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
            output.write(jsonContent.toByteArray())
        }
    }

    /**
     * Load user information
     *
     * @param context
     * @param firstName
     * @param lastName
     * @return
     */
    fun loadUserInformation(context: Context, firstName: String, lastName: String): User? {
        val gson = Gson()
        lateinit var user: User
        val fileName = "${firstName.lowercase()}_${lastName.lowercase()}.json"
        val file = File(context.filesDir, fileName)

        return if (file.exists() && file.isFile) {
            val content = file.readText()
            user = gson.fromJson(content, User::class.java).also {
                Log.d(TAG, "loadAllUsersInformation: Loaded user profile from ${file.name}")
            }
            user
        } else {
            Log.d(TAG, "loadUserInformation: User $firstName $lastName does not exist")
            null
        }
    }
}

/**
 * Load all users information
 *
 * @param context
 * @return
 */
fun loadAllUsersInformation(context: Context): MutableList<User> {
    val gson = Gson()
    val userList = mutableListOf<User>()
    val regex = Regex("""\w+_\w+.json""")

    val files = context.filesDir.listFiles(FileFilter { it.isFile && regex.matches(it.name) })!!
    if (files.isEmpty())
        Log.i(TAG, "loadAllUsersInformation: No user file found !")
    else
        files.forEach { file ->
            val content = file.readText()
            val user = gson.fromJson(content, User::class.java)
            userList.add(user)
            Log.d(
                TAG,
                "loadAllUsersInformation: Loaded user profile from ${file.name}\nUtilisateur : ${user.firstName} ${user.lastName}, ${user.age} ans"
            )
        }

    return userList
}

/**
 * Store all users information
 *
 * @param context
 * @param userList
 */
fun storeAllUsersInformation(context: Context, userList: MutableList<User>) {
    userList.forEach { user ->
        user.storeUserInformation(context)
    }
    Log.d(TAG, "storeAllUsersInformation: Stored all user profiles")
}

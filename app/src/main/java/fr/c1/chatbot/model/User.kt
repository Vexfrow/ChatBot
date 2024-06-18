package fr.c1.chatbot.model

import com.google.gson.Gson
import fr.c1.chatbot.model.activity.Type
import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileFilter

private const val TAG = "ProfilUtilisateur"

class User(
    /**
     * Nom de l'utilisateur (utilisé, en plus du prénom, pour différencier les utilisateurs)
     */
    var lastName: String = "",
    /**
     * Prénom de l'utilisateur (utilisé pour l'affichage)
     */
    var firstName: String = "",
    /**
     * Age de l'utilisateur
     */
    var age: Int = -1,
    /**
     * Lise de villes de l'utilisateur
     */
    private val _cities: MutableList<String> = mutableListOf(),
    /**
     * Type d'activité souhaitée par l'utilisateur
     */
    private var _types: MutableList<Type> = mutableListOf(),
    /**
     * Passions de l'utilisateur
     */
    private val _passions: MutableList<String> = mutableListOf(),
    /**
     * Liste des préférences hebdomadaires de l'utilisateur
     */
    private var _weeklyPreferences: MutableList<WeeklyPreference> = mutableListOf()
) {

    /**
     * Constructeur secondaire
     */
    constructor(lastName: String, firstName: String, age: Int) : this() {
        this.lastName = lastName
        this.firstName = firstName
        this.age = age
    }

    /**
     * Ajouter une ville
     */
    fun addCity(city: String) {
        _cities.add(city)
    }

    /**
     * Ajouter un type d'activité
     */
    fun addType(type: Type) {
        _types.add(type)
    }

    /**
     * Ajouter une passion
     */
    fun addPassion(passion: String) {
        _passions.add(passion)
    }

    /**
     * Ajouter une préférence hebdomadaire
     */
    fun addWeeklyPreference(day: String, houe: String, duration: Int) {
        _weeklyPreferences.add(WeeklyPreference(day, houe, duration))
    }

    /**
     * Supprimer une préférence hebdomadaire
     */
    fun removeWeeklyPeference(day: String, hour: String, duration: Int) {
        _weeklyPreferences.remove(WeeklyPreference(day, hour, duration))
    }

    /**
     * Supprimer toutes les préférences hebdomadaires
     */
    fun clearWeeklyPreferences() {
        _weeklyPreferences.clear()
    }

    /**
     * Supprimer une ville
     */
    fun removeCity(city: String) {
        _cities.remove(city)
    }

    /**
     * Supprimer toutes les villes
     */
    fun clearCities() {
        _cities.clear()
    }

    /**
     * Supprimer une passion
     */
    fun removePassion(passion: String) {
        _passions.remove(passion)
    }

    /**
     * Supprimer toutes les passions
     */
    fun clearPassions() {
        _passions.clear()
    }

    /**
     * Supprimer un type d'activité
     */
    fun removeType(type: Type) {
        _types.remove(type)
    }

    /**
     * Supprimer toutes les activités
     */
    fun clearTypes() {
        _types.clear()
    }

    /**
     * Récupérer les villes
     */
    val cities: List<String> get() = _cities

    /**
     * Récupérer les types d'activité
     */
    val types: List<Type> get() = _types

    fun hasPassion(passion: String): Boolean = passions.contains(passion)

    /**
     * Récupérer les passions
     */
    val passions: List<String>
        get() = _passions

    /**
     * Récupérer les préférences hebdomadaires
     */
    val weeklyPreferences: List<WeeklyPreference> get() = _weeklyPreferences

    /**
     * Stocker les informations de l'utilisateur dans un fichier json
     */
    fun storeUserInformation(context: Context) {
        val fileName = "${lastName.lowercase()}_${firstName.lowercase()}.json"

        // Construire le contenu JSON
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

        // Ecrire le contenu JSON dans le fichier
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
            output.write(jsonContent.toByteArray())
        }
    }

    /**
     * Charger les informations de l'utilisateur depuis un fichier json
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
 * Charger toutes les informations des utilisateurs
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
 * Sauvegarder toute la liste des utilisateurs
 */
fun storeAllUsersInformation(context: Context, userList: MutableList<User>) {
    userList.forEach { user ->
        user.storeUserInformation(context)
    }
    Log.d(TAG, "storeAllUsersInformation: Stored all user profiles")
}

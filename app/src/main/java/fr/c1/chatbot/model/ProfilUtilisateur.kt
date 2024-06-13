package fr.c1.chatbot.model

import com.google.gson.Gson
import fr.c1.chatbot.model.activity.Type
import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileFilter

private const val TAG = "ProfilUtilisateur"

class ProfilUtilisateur(
    /**
     * Nom de l'utilisateur (utilisé, en plus du prénom, pour différencier les utilisateurs)
     */
    var nom: String = "",
    /**
     * Prénom de l'utilisateur (utilisé pour l'affichage)
     */
    var prenom: String = "",
    /**
     * Age de l'utilisateur
     */
    var age: Int = -1,
    /**
     * Lise de villes de l'utilisateur
     */
    private val _villesList: MutableList<String> = mutableListOf(),
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
    private var _preferencesHebdomadaires: MutableList<WeeklyPreference> = mutableListOf()
) {

    /**
     * Constructeur secondaire
     */
    constructor(nom: String, prenom: String, age: Int) : this() {
        this.nom = nom
        this.prenom = prenom
        this.age = age
    }

    /**
     * Ajouter une ville
     */
    fun addVille(ville: String) {
        _villesList.add(ville)
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
    fun addPreferenceHebdo(jour: String, heure: String, duree: Int) {
        _preferencesHebdomadaires.add(WeeklyPreference(jour, heure, duree))
    }

    /**
     * Supprimer une préférence hebdomadaire
     */
    fun removePreferenceHebdo(jour: String, heure: String, duree: Int) {
        _preferencesHebdomadaires.remove(WeeklyPreference(jour, heure, duree))
    }

    /**
     * Supprimer toutes les préférences hebdomadaires
     */
    fun removeAllPreferencesHebdo() {
        _preferencesHebdomadaires.clear()
    }

    /**
     * Supprimer une ville
     */
    fun removeVille(ville: String) {
        _villesList.remove(ville)
    }

    /**
     * Supprimer toutes les villes
     */
    fun removeAllVilles() {
        _villesList.clear()
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
    fun removeAllPassions() {
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
    fun removeAllTypes() {
        _types.clear()
    }

    /**
     * Récupérer les villes
     */
    val cities: List<String> get() = _villesList

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
    val weeklyPreferences: List<WeeklyPreference> get() = _preferencesHebdomadaires

    /**
     * Stocker les informations de l'utilisateur dans un fichier json
     */
    fun storeUserInformation(context: Context) {
        val fileName = "${nom.lowercase()}_${prenom.lowercase()}.json"

        // Construire le contenu JSON
        val jsonContent = """
        {
            "nom": "$nom",
            "prenom": "$prenom",
            "age": $age,
            "villes": ${_villesList.joinToString(prefix = "[", postfix = "]") { "\"$it\"" }},
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
    fun loadUserInformation(context: Context, nom: String, prenom: String): ProfilUtilisateur? {
        val gson = Gson()
        lateinit var user: ProfilUtilisateur
        val fileName = "${nom.lowercase()}_${prenom.lowercase()}.json"
        val file = File(context.filesDir, fileName)

        return if (file.exists() && file.isFile) {
            val content = file.readText()
            user = gson.fromJson(content, ProfilUtilisateur::class.java).also {
                Log.d(TAG, "loadAllUsersInformation: Loaded user profile from ${file.name}")
            }
            user
        } else {
            Log.d(TAG, "loadUserInformation: User $nom $prenom does not exist")
            null
        }
    }
}

/**
 * Charger toutes les informations des utilisateurs
 */
fun loadAllUsersInformation(context: Context): MutableList<ProfilUtilisateur> {
    val gson = Gson()
    val userList = mutableListOf<ProfilUtilisateur>()
    val regex = Regex("""\w+_\w+.json""")

    val files = context.filesDir.listFiles(FileFilter { it.isFile && regex.matches(it.name) })!!
    if (files.isEmpty())
        Log.i(TAG, "loadAllUsersInformation: No user file found !")
    else
        files.forEach { file ->
            val content = file.readText()
            val user = gson.fromJson(content, ProfilUtilisateur::class.java)
            userList.add(user)
            Log.d(
                TAG,
                "loadAllUsersInformation: Loaded user profile from ${file.name}\nUtilisateur : ${user.prenom} ${user.nom}, ${user.age} ans"
            )
        }

    return userList
}

/**
 * Sauvegarder toute la liste des utilisateurs
 */
fun storeAllUsersInformation(context: Context, userList: MutableList<ProfilUtilisateur>) {
    userList.forEach { user ->
        user.storeUserInformation(context)
    }
    Log.d(TAG, "storeAllUsersInformation: Stored all user profiles")
}

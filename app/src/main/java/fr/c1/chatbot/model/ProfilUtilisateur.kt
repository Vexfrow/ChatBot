package fr.c1.chatbot.model

import android.content.Context
import android.location.Location
import android.util.Log
import com.google.gson.Gson
import fr.c1.chatbot.model.activity.Type
import java.io.File
import java.util.Date

private const val TAG = "ProfilUtilisateur"

class ProfilUtilisateur(
    /**
     * Nom de l'utilisateur (utilisé, en plus du prénom, pour différencier les utilisateurs)
     */
    var nom: String = "",
    /**
     * Prénom de l'utilisateur (utilisé pour l'affichage)
     */
    private var prenom: String = "",
    /**
     * Age de l'utilisateur
     */
    private var age: Int = -1,
    /**
     * Date souhaitée par l'utilisateur
     */
    private var date: Date? = null,
    /**
     * Lise de villes de l'utilisateur
     */
    private val villesList: MutableList<String> = mutableListOf("Grenoble"),
    /**
     * Distance souhaitée par l'utilisateur
     */
    private var distance: Int = 0,
    /**
     * Type d'activité souhaitée par l'utilisateur
     */
    private var types: MutableList<Type> = mutableListOf(),
    /**
     * Localisation de l'utilisateur
     */
    private var localisation: Location = Location(""),
    /**
     * Passions de l'utilisateur
     */
    private val passions: MutableList<String> = mutableListOf(),
    /**
     * Liste des préférences hebdomadaires de l'utilisateur
     */
    private var preferencesHebdomadaires: MutableList<PreferencesHebdo> = mutableListOf()
) {

    /**
     * Ajouter une ville
     */
    fun addVille(ville: String) {
        villesList.add(ville)
    }

    /**
     * Ajouter une date
     */
    fun setDate(date: Date) {
        this.date = date
    }

    /**
     * Ajouter une distance
     */
    fun setDistance(distance: Int) {
        this.distance = distance
    }

    /**
     * Ajouter un type d'activité
     */
    fun addType(type: Type) {
        types.add(type)
    }

    /**
     * Ajouter une localisation
     */
    fun setLocalisation(localisation: Location) {
        this.localisation = localisation
    }

    /**
     * Ajouter une passion
     */
    fun addPassion(passion: String) {
        passions.add(passion)
    }

    /**
     * Ajouter une préférence hebdomadaire
     */
    fun addPreferenceHebdo(jour: String, heure: String, duree: Int) {
        preferencesHebdomadaires.add(PreferencesHebdo(jour, heure, duree))
    }

    /**
     * Supprimer une préférence hebdomadaire
     */
    fun removePreferenceHebdo(jour: String, heure: String, duree: Int) {
        preferencesHebdomadaires.remove(PreferencesHebdo(jour, heure, duree))
    }

    /**
     * Supprimer toutes les préférences hebdomadaires
     */
    fun removeAllPreferencesHebdo() {
        preferencesHebdomadaires.clear()
    }

    /**
     * Supprimer une ville
     */
    fun removeVille(ville: String) {
        villesList.remove(ville)
    }

    /**
     * Supprimer toutes les villes
     */
    fun removeAllVilles() {
        villesList.clear()
    }

    /**
     * Supprimer une passion
     */
    fun removePassion(passion: String) {
        passions.remove(passion)
    }

    /**
     * Supprimer toutes les passions
     */
    fun removeAllPassions() {
        passions.clear()
    }

    /**
     * Supprimer une date
     */
    fun removeDate() {
        date = null
    }

    /**
     * Supprimer une distance
     */
    fun removeDistance() {
        distance = 0
    }

    /**
     * Supprimer un type d'activité
     */
    fun removeType(type: Type) {
        types.remove(type)
    }

    /**
     * Supprimer toutes les activités
     */
    fun removeAllTypes() {
        types.clear()
    }

    /**
     * Supprimer une localisation
     */
    fun removeLocalisation() {
        localisation = Location("")
    }

    /**
     * Récupérer les villes
     */
    fun getVilles(): MutableList<String> {
        return villesList
    }

    /**
     * Récupérer la date
     */
    fun getDate(): Date? {
        return date
    }

    /**
     * Récupérer la distance
     */
    fun getDistance(): Int {
        return distance
    }

    /**
     * Récupérer les types d'activité
     */
    fun getTypes(): MutableList<Type> {
        return types
    }

    /**
     * Récupérer la localisation
     */
    fun getLocalisation(): Location {
        return localisation
    }

    /**
     * Récupérer les passions
     */
    fun getPassions(): MutableList<String> {
        return passions
    }

    /**
     * Récupérer les préférences hebdomadaires
     */
    fun getPreferencesHebdo(): MutableList<PreferencesHebdo> {
        return preferencesHebdomadaires
    }

    /**
     * Récupérer le prénom
     */
    fun getPrenom(): String {
        return prenom
    }

    /**
     * Récupérer l'âge
     */
    fun getAge(): Int {
        return age
    }

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
            "date": "$date",
            "villes": ${villesList.joinToString(prefix = "[", postfix = "]") { "\"$it\"" }},
            "distance": $distance,
            "types": ${types.joinToString(prefix = "[", postfix = "]") { "\"$it\"" }},
            "localisation": {
                "latitude": ${localisation.latitude},
                "longitude": ${localisation.longitude}
            },
            "passions": ${passions.joinToString(prefix = "[", postfix = "]") { "\"$it\"" }},
            "preferencesHebdo": ${
            preferencesHebdomadaires.joinToString(
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
        val fileName = "${nom.lowercase()}${prenom.lowercase()}.json"
        val file = File(context.filesDir, fileName)
        return if (file.exists()) {
            val content = file.readText()
            val gson = Gson()
            gson.fromJson(content, ProfilUtilisateur::class.java).also {
                Log.d(TAG, "loadUserInformation: User information loaded")
            }
        } else {
            Log.d(TAG, "loadUserInformation: File does not exist")
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

    context.filesDir.listFiles()?.forEach { file ->
        if (file.isFile && regex.matches(file.name)) {
            val content = file.readText()
            val user = gson.fromJson(content, ProfilUtilisateur::class.java)
            userList.add(user)
            Log.d(TAG, "loadAllUsersInformation: Loaded user profile from ${file.name}")
        }
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
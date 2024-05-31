package fr.c1.chatbot.model

import android.location.Location
import android.util.Log
import fr.c1.chatbot.model.activity.Type
import org.json.JSONObject
import java.io.File

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
    private var date: String = "",
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
    private var preferencesHebdomadaires: MutableList<PreferencesHebdo> = mutableListOf()) {

    /**
     * Ajouter une ville
     */
    fun addVille(ville: String) {
        villesList.add(ville)
    }

    /**
     * Ajouter une date
     */
    fun setDate(date: String) {
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
        date = ""
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
    fun getDate(): String {
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
    fun storeUserInformation() {
        val fileName = nom.lowercase() + "_" + prenom.lowercase() + ".json"
        // Vérifier si le fichier existe déjà (nom_prenom.json)
        if (File(fileName).exists()) {
            // Si oui, le supprimer
            File(fileName).delete()
        }
        // Créer un nouveau fichier
        File(fileName).createNewFile()
        // Ecrire les informations de l'utilisateur dans le fichier
        File(fileName).writeText(
            """
            {
                "nom": "$nom",
                "prenom": "$prenom",
                "age": $age,
                "date": "$date",
                "villes": ${villesList.joinToString(prefix = "[", postfix = "]")},
                "distance": $distance,
                "types": ${types.joinToString(prefix = "[", postfix = "]")},
                "localisation": {
                    "latitude": ${localisation.latitude},
                    "longitude": ${localisation.longitude}
                },
                "passions": ${passions.joinToString(prefix = "[", postfix = "]")},
                "preferencesHebdo": ${preferencesHebdomadaires.joinToString(prefix = "[", postfix = "]")}
            }
        """.trimIndent()
        )
    }

    /**
     * Charger les informations de l'utilisateur depuis un fichier json
     */
    fun loadUserInformation() {
        // Vérifier si le fichier existe (nom_prenom.json)
        val fileName = nom.lowercase() + "_" + prenom.lowercase() + ".json"
        if (File(fileName).exists()) {
            // Lire le contenu du fichier
            val content = File(fileName).readText()
            // Parser le contenu du fichier
            val json = JSONObject(content)
            // Récupérer les informations de l'utilisateur
            nom = json.getString("nom")
            prenom = json.getString("prenom")
            age = json.getInt("age")
            date = json.getString("date")
            // itérateur sur les villes
            for (i in 0 until json.getJSONArray("villes").length()) {
                villesList.add(json.getJSONArray("villes").getString(i))
            }
            distance = json.getInt("distance")
            // itérateur sur les types
            for (i in 0 until json.getJSONArray("types").length()) {
                types.add(Type.valueOf(json.getJSONArray("types").getString(i)))
            }
            val localisationJson = json.getJSONObject("localisation")
            localisation.latitude = localisationJson.getDouble("latitude")
            localisation.longitude = localisationJson.getDouble("longitude")
            // itérateur sur les passions
            for (i in 0 until json.getJSONArray("passions").length()) {
                passions.add(json.getJSONArray("passions").getString(i))
            }
            // itérateur sur les préférences hebdomadaires
            for (i in 0 until json.getJSONArray("preferencesHebdo").length()) {
                val preference = json.getJSONArray("preferencesHebdo").getJSONObject(i)
                preferencesHebdomadaires.add(
                    PreferencesHebdo(
                        preference.getString("jour"),
                        preference.getString("heure"),
                        preference.getInt("duree")
                    )
                )
            }
            Log.d(TAG, "loadUserInformation: User information loaded")
        } else {
            // Si le fichier n'existe pas, on ne fait rien
            Log.d(TAG, "loadUserInformation: File does not exist")
        }
    }
}
package fr.c1.chatbot.model

import fr.c1.chatbot.R
import fr.c1.chatbot.model.activity.AbstractActivity
import fr.c1.chatbot.model.activity.Associations
import fr.c1.chatbot.model.activity.Contenus
import fr.c1.chatbot.model.activity.Edifices
import fr.c1.chatbot.model.activity.EquipementsSport
import fr.c1.chatbot.model.activity.Expositions
import fr.c1.chatbot.model.activity.Festivals
import fr.c1.chatbot.model.activity.Jardins
import fr.c1.chatbot.model.activity.Musees
import fr.c1.chatbot.model.activity.Sites
import fr.c1.chatbot.model.activity.Type
import android.app.Application
import android.location.Location
import android.util.Log
import fr.c1.chatbot.ChatBot
import fr.c1.chatbot.utils.parseCsv
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.io.BufferedInputStream
import java.io.InputStream
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import android.content.Context

// Fichiers CSV venant du site data.gouv.fr
class ActivitiesRepository {

    private val TAG = "ActivitiesRepository"

    companion object {
        val passionList: Set<String>
            get() = Associations.passions union Contenus.passions union Sites.passions union Musees.passions union Jardins.passions union Festivals.passions union Expositions.passions union EquipementsSport.passions union Edifices.passions
    }

    private val listeVillesDisponible = sortedSetOf<String>()


    private var date: String = null.toString()

    private var distance = 10 // 10 km par défaut

    private var location: Location = Location("")

    /**
     * Liste des musées
     */
    private val museesList = mutableListOf<Musees>()

    /**
     * Liste des sites patrimoniaux
     */
    private val sitesList = mutableListOf<Sites>()

    /**
     * Liste des expositions
     */
    private val expositionsList = mutableListOf<Expositions>()

    /**
     * Liste des contenus culturels
     */
    private val contenusList = mutableListOf<Contenus>()

    /**
     * Liste des édifices
     */
    private val edificesList = mutableListOf<Edifices>()

    /**
     * Liste des jardins
     */
    private val jardinsList = mutableListOf<Jardins>()

    /**
     * Liste des festivals
     */
    private val festivalsList = mutableListOf<Festivals>()

    /**
     * Liste des équipements sportifs
     */
    private val equipementsSportList = mutableListOf<EquipementsSport>()

    /**
     * Liste des associations
     */
    private val associationsList = mutableListOf<Associations>()

    val all: List<AbstractActivity>
        get() = museesList + sitesList + expositionsList + contenusList + edificesList + jardinsList + festivalsList + equipementsSportList + associationsList

    /**
     * Récupérer la liste des musées
     */
    fun getMuseesList(): List<Musees> {
        return museesList
    }

    fun getVillesDisponible(): Collection<String> {
        return listeVillesDisponible
    }

    /**
     * Récupérer la liste des sites patrimoniaux
     */
    fun getSitesList(): List<Sites> {
        return sitesList
    }

    /**
     * Récupérer la liste des expositions
     */
    fun getExpositionsList(): List<Expositions> {
        return expositionsList
    }

    /**
     * Récupérer la liste des contenus culturels
     */
    fun getContenusList(): List<Contenus> {
        return contenusList
    }

    /**
     * Récupérer la liste des édifices
     */
    fun getEdificesList(): List<Edifices> {
        return edificesList
    }

    /**
     * Récupérer la liste des jardins
     */
    fun getJardinsList(): List<Jardins> {
        return jardinsList
    }

    /**
     * Récupérer la liste des festivals
     */
    fun getFestivalsList(): List<Festivals> {
        return festivalsList
    }

    /**
     * Récupérer la liste des équipements sportifs
     */
    fun getEquipementsSportList(): List<EquipementsSport> {
        return equipementsSportList
    }

    /**
     * Récupérer la liste des associations
     */
    fun getAssociationsList(): List<Associations> {
        return associationsList
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
     * Récupérer la localisation
     */
    fun getLocation(): Location {
        return location
    }

    /**
     * Ajouter une ville à la liste des villes disponibles
     */
    private fun addVilleDispo(str: String) {
        if (str.isBlank())
            return

        if (str.any(Char::isDigit))
            return

        val tmp = str
            .trim('\"')
            .lowercase(Locale.getDefault())
            .replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                else it.toString()
            }

        if (!listeVillesDisponible.contains(tmp))
            listeVillesDisponible.add(tmp)
    }

    /**
     * CHoisir la date
     */
    fun setDate(date: String) {
        this.date = date
    }

    /**
     * Choisir la distance
     */
    fun setDistance(distance: Int) {
        this.distance = distance
    }

    /**
     * Choisir la location
     */
    fun setLocation(location: Location) {
        this.location = location
    }

    /**
     * Initialiser les musées
     */
    fun initMusees(app: Application) {
        // Lire le fichier csv
        val csvIS: InputStream =
            BufferedInputStream(app.resources.openRawResource(R.raw.liste_musees_france))
        // Parser le fichier csv avec des ; comme séparateur (attention, ceux entre guillemets ne sont pas pris en compte)
        val csvData = parseCsv(csvIS)
        // Créer la liste des sites patrimoniaux
        for (strings in csvData.drop(1)) {
            val region = strings[0]
            val departement = strings[1]
            val identifiant = strings[2]
            val commune = strings[3]
            val nom = strings[4]
            val adresse = strings[5]
            val lieu = strings[6]
            val codePostal = strings[7]
            val telephone = strings[8]
            val url = strings[9]
            val latitude = strings[10].toDouble()
            val longitude = strings[11].toDouble()
            val activity = Musees(
                region,
                departement,
                identifiant,
                commune,
                nom,
                adresse,
                lieu,
                codePostal,
                telephone,
                url,
                true,
                latitude,
                longitude
            )
            museesList.add(activity)
            addVilleDispo(commune)
        }
    }

    /**
     * Initialiser les sites patrimoniaux
     */
    fun initSites(app: Application) {
        // Lire le fichier csv
        val csvIS: InputStream =
            BufferedInputStream(app.resources.openRawResource(R.raw.liste_sites_patrimoniaux))

        val csvData = parseCsv(csvIS)

        // Créer la liste des sites patrimoniaux
        for (strings in csvData.drop(1)) {
            val region = strings[1]
            val departement = strings[3]
            val commune = strings[4]
            val latitude = strings[16].toDouble()
            val longitude = strings[17].toDouble()
            val activity = Sites(
                region,
                departement,
                commune,
                true,
                latitude,
                longitude
            )
            sitesList.add(activity)
            addVilleDispo(commune)
        }
    }

    /**
     * Initialiser les expositions
     */
    fun initExpositions(app: Application) {
        // Lire le fichier csv
        val csvIS: InputStream =
            BufferedInputStream(app.resources.openRawResource(R.raw.liste_expositions))

        val csvData = parseCsv(csvIS)

        // Créer la liste des sites patrimoniaux
        for (strings in csvData.drop(1)) {
            val region = strings[2]
            val departement = strings[6]
            val commune = strings[3]
            val identifiant = strings[1]
            val nom = strings[4]
            val url = strings[7]
            val latitude = strings[8].split(",")[0].toDouble()
            val longitude = strings[8].split(",")[1].toDouble()
            val activity = Expositions(
                region,
                departement,
                identifiant,
                commune,
                nom,
                url,
                true,
                latitude,
                longitude
            )
            expositionsList.add(activity)
            addVilleDispo(commune)
        }
    }

    /**
     * Initialiser les contenus culturels
     */
    fun initContenus(app: Application) {
        // Lire le fichier csv
        val csvIS: InputStream =
            BufferedInputStream(app.resources.openRawResource(R.raw.liste_culture))

        val csvData = parseCsv(csvIS)

        // Créer la liste des sites patrimoniaux
        for (strings in csvData.drop(1)) {
            val identifiant = strings[0]
            val commune = strings[4]
            val nom = strings[1]
            val adresse = strings[2]
            val lieu = strings[6]
            val codePostal = strings[3]
            val url = strings[5]
            val latitude = strings[20].split(",")[0].toDouble()
            val longitude = strings[20].split(",")[1].toDouble()
            val activity = Contenus(
                identifiant,
                commune,
                nom,
                adresse,
                lieu,
                codePostal,
                url,
                true,
                latitude,
                longitude
            )
            contenusList.add(activity)
            addVilleDispo(commune)
        }
    }

    /**
     * Initialiser les édifices
     */
    fun initEdifices(app: Application) {
        // Lire le fichier csv
        // reference_de_la_notice;region;commune;adresse_forme_editoriale;coordonnees;titre_courant;departement_en_lettres
        val csvIS: InputStream =
            BufferedInputStream(app.resources.openRawResource(R.raw.liste_edifices_architecture_contemporaine))

        val csvData = parseCsv(csvIS)

        var latitude: Double
        var longitude: Double

        // Créer la liste des edifices d'architecture contemporaine
        for (strings in csvData.drop(1)) {
            val region = strings[1]
            val departement = strings[6]
            val commune = strings[2]
            val adresse = strings[3]
            val nom = strings[5]
            if (strings[4].isNotEmpty()) {
                latitude = strings[4].split(",")[0].toDouble()
                longitude = strings[4].split(",")[1].toDouble()
            } else {
                latitude = -1.0
                longitude = -1.0
            }
            val activity = Edifices(
                region,
                departement,
                commune,
                nom,
                adresse,
                true,
                latitude,
                longitude
            )
            edificesList.add(activity)
            addVilleDispo(commune)
        }
    }

    /**
     * Initialiser les jardins
     */
    fun initJardins(app: Application) {
        // Lire le fichier csv
        val csvIS: InputStream =
            BufferedInputStream(app.resources.openRawResource(R.raw.liste_jardins_remarquables))

        val csvData = parseCsv(csvIS)

        // Créer la liste des jardins remarquables
        for (strings in csvData.drop(1)) {
            val region = strings[2]
            val departement = strings[3]
            val commune = strings[8]
            val nom = strings[0]
            val adresse = strings[4]
            val codePostal = strings[1]
            val accessible = strings[21].lowercase().contains("ouvert")
            val latitude = strings[12].toDouble()
            val longitude = strings[13].toDouble()
            val activity = Jardins(
                region,
                departement,
                commune,
                nom,
                adresse,
                codePostal,
                accessible,
                latitude,
                longitude
            )
            jardinsList.add(activity)
            addVilleDispo(commune)
        }
    }

    /**
     * Initialiser les festivals
     */
    fun initFestivals(app: Application) {
        // Lire le fichier csv
        val csvIS: InputStream =
            BufferedInputStream(app.resources.openRawResource(R.raw.liste_festivals))

        val csvData = parseCsv(csvIS)

        var latitude: Double
        var longitude: Double

        // Créer la liste des festivals
        for (strings in csvData.drop(1)) {
            val region = strings[2]
            val departement = strings[3]
            val commune = strings[4]
            val nom = strings[0]
            val adresse = strings[12]
            val codePostal = strings[5]
            val discipline = strings[18]
            if (strings[28].isNotEmpty()) {
                latitude = strings[28].split(",")[0].toDouble()
                longitude = strings[28].split(",")[1].toDouble()
            } else {
                latitude = -1.0
                longitude = -1.0
            }
            val activity = Festivals(
                region,
                departement,
                commune,
                nom,
                adresse,
                codePostal,
                discipline,
                true,
                latitude,
                longitude
            )
            festivalsList.add(activity)
            addVilleDispo(commune)
        }
    }

    /**
     * Initialiser les équipements sportifs
     */
    fun initEquipementsSport(app: Application) {
        // Lire le fichier csv
        // numinstallation;nominstallation;adresse;codepostal;commune;typequipement;latitude;longitude
        val csvIS: InputStream =
            BufferedInputStream(app.resources.openRawResource(R.raw.liste_equipements_sportifs))

        val csvData = parseCsv(csvIS)

        var latitude: Double
        var longitude: Double

        // Créer la liste des équipements sportifs
        for (strings in csvData.drop(1)) {
            val departement = strings[3].substring(0, 2)
            val commune = strings[4]
            val nom = strings[1]
            val adresse = strings[2]
            val codePostal = strings[3]
            if (strings[6].isNotEmpty()) {
                latitude = strings[6].toDouble()
            } else {
                latitude = -1.0
            }
            if (strings[7].isNotEmpty()) {
                longitude = strings[7].toDouble()
            } else {
                longitude = -1.0
            }
            val activity = EquipementsSport(
                departement,
                commune,
                nom,
                adresse,
                codePostal,
                true,
                latitude,
                longitude
            )
            equipementsSportList.add(activity)
            addVilleDispo(commune)
        }
    }

    /**
     * Initialiser les associations
     */
    fun initAsso(app: Application) {
        // Lire le fichier csv
        // id;titre;adr1;adrs_codepostal;libcom;siteweb;latitude;longitude
        val csvIS: InputStream =
            BufferedInputStream(app.resources.openRawResource(R.raw.liste_asso))

        val csvData = parseCsv(csvIS)

        var latitude: Double
        var longitude: Double

        // Créer la liste des associations
        for (strings in csvData.drop(1)) {
            var departement = ""
            if (strings[3].length > 2) {
                departement = strings[3].substring(0, 2)
            }
            val identifiant = strings[0]
            val commune = strings[4]
            val nom = strings[1]
            val adresse = strings[2]
            val codePostal = strings[3]
            val accessible = true
            val url = strings[5]
            latitude = if (strings[6].isNotEmpty()) {
                strings[6].toDouble()
            } else {
                -1.0
            }
            longitude = if (strings[7].isNotEmpty()) {
                strings[7].toDouble()
            } else {
                -1.0
            }
            val activity = Associations(
                departement,
                identifiant,
                commune,
                nom,
                adresse,
                codePostal,
                accessible,
                latitude,
                longitude,
                url
            )
            associationsList.add(activity)
        }
    }

    /**
     * Initialiser toutes les listes
     */
    fun initAll(app: Application) {
        initMusees(app)
        initSites(app)
        initExpositions(app)
        initContenus(app)
        initEdifices(app)
        initJardins(app)
        initFestivals(app)
        initEquipementsSport(app)
        initAsso(app)
    }

    /**
     * Afficher une liste
     */
    fun <T> displayList(list: List<T>) {
        list.forEach { println(it) }
    }

    /**
     * Afficher toutes les listes
     */
    fun displayAll() {
        displayMusees()
        displaySites()
        displayExpositions()
        displayContenus()
        displayEdifices()
        displayJardins()
        displayFestivals()
        displayEquipementsSport()
        displayAssociations()
    }

    /**
     * Afficher la liste des musées
     */
    fun displayMusees() {
        displayList(museesList)
    }

    /**
     * Afficher la liste des sites patrimoniaux
     */
    fun displaySites() {
        displayList(sitesList)
    }

    /**
     * Afficher la liste des expositions
     */
    fun displayExpositions() {
        displayList(expositionsList)
    }

    /**
     * Afficher la liste des contenus culturels
     */
    fun displayContenus() {
        displayList(contenusList)
    }

    /**
     * Afficher la liste des édifices
     */
    fun displayEdifices() {
        displayList(edificesList)
    }

    /**
     * Afficher la liste des jardins
     */
    fun displayJardins() {
        displayList(jardinsList)
    }

    /**
     * Afficher la liste des festivals
     */
    fun displayFestivals() {
        displayList(festivalsList)
    }

    /**
     * Afficher la liste des équipements sportifs
     */
    fun displayEquipementsSport() {
        displayList(equipementsSportList)
    }

    /**
     * Afficher la liste des associations
     */
    fun displayAssociations() {
        displayList(associationsList)
    }

    /**
     * Trier la liste par région (si le critère région est présent)
     */
    fun <T> trierParRegion(list: List<T>): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Musees::class -> list.sortedBy { (it as Musees).region }
            Sites::class -> list.sortedBy { (it as Sites).region }
            Expositions::class -> list.sortedBy { (it as Expositions).region }
            Edifices::class -> list.sortedBy { (it as Edifices).region }
            Jardins::class -> list.sortedBy { (it as Jardins).region }
            Festivals::class -> list.sortedBy { (it as Festivals).region }
            else -> list
        }
    }

    /**
     * Sélectionner les éléments par région (si le critère région est présent)
     */
    fun <T> selectionnerParRegion(list: List<T>, region: String): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Musees::class -> list.filter {
                (it as Musees).region.lowercase().contains(region.lowercase())
            }

            Sites::class -> list.filter {
                (it as Sites).region.lowercase().contains(region.lowercase())
            }

            Expositions::class -> list.filter {
                (it as Expositions).region.lowercase().contains(region.lowercase())
            }

            Edifices::class -> list.filter {
                (it as Edifices).region.lowercase().contains(region.lowercase())
            }

            Jardins::class -> list.filter {
                (it as Jardins).region.lowercase().contains(region.lowercase())
            }

            Festivals::class -> list.filter {
                (it as Festivals).region.lowercase().contains(region.lowercase())
            }

            else -> list
        }
    }

    /**
     * Trier la liste par département (si le critère département est présent)
     */
    fun <T> trierParDepartement(list: List<T>): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Musees::class -> list.sortedBy { (it as Musees).departement }
            Sites::class -> list.sortedBy { (it as Sites).departement }
            Expositions::class -> list.sortedBy { (it as Expositions).departement }
            Edifices::class -> list.sortedBy { (it as Edifices).departement }
            Jardins::class -> list.sortedBy { (it as Jardins).departement }
            Festivals::class -> list.sortedBy { (it as Festivals).departement }
            EquipementsSport::class -> list.sortedBy { (it as EquipementsSport).departement }
            Associations::class -> list.sortedBy { (it as Associations).departement }
            else -> list
        }
    }

    /**
     * Sélectionner les éléments par département (si le critère département est présent)
     */
    fun <T> selectionnerParDepartement(list: List<T>, departement: String): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Musees::class -> list.filter {
                (it as Musees).departement.lowercase().contains(departement.lowercase())
            }

            Sites::class -> list.filter {
                (it as Sites).departement.lowercase().contains(departement.lowercase())
            }

            Expositions::class -> list.filter {
                (it as Expositions).departement.lowercase().contains(departement.lowercase())
            }

            Edifices::class -> list.filter {
                (it as Edifices).departement.lowercase().contains(departement.lowercase())
            }

            Jardins::class -> list.filter {
                (it as Jardins).departement.lowercase().contains(departement.lowercase())
            }

            Festivals::class -> list.filter {
                (it as Festivals).departement.lowercase().contains(departement.lowercase())
            }

            EquipementsSport::class -> list.filter {
                (it as EquipementsSport).departement.lowercase().contains(departement.lowercase())
            }

            Associations::class -> list.filter {
                (it as Associations).departement.lowercase().contains(departement.lowercase())
            }

            else -> list
        }
    }

    /**
     * Trier la liste par commune (si le critère commune est présent)
     */
    fun <T> trierParCommune(list: List<T>): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Musees::class -> list.sortedBy { (it as Musees).commune }
            Sites::class -> list.sortedBy { (it as Sites).commune }
            Expositions::class -> list.sortedBy { (it as Expositions).commune }
            Contenus::class -> list.sortedBy { (it as Contenus).commune }
            Edifices::class -> list.sortedBy { (it as Edifices).commune }
            Jardins::class -> list.sortedBy { (it as Jardins).commune }
            Festivals::class -> list.sortedBy { (it as Festivals).commune }
            EquipementsSport::class -> list.sortedBy { (it as EquipementsSport).commune }
            Associations::class -> list.sortedBy { (it as Associations).commune }
            else -> list
        }
    }

    /**
     * Sélectionner les éléments par commune (si le critère commune est présent)
     */
    fun <T> selectionnerParCommune(list: List<T>, commune: String): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Musees::class -> list.filter {
                (it as Musees).commune.lowercase().contains(commune.lowercase())
            }

            Sites::class -> list.filter {
                (it as Sites).commune.lowercase().contains(commune.lowercase())
            }

            Expositions::class -> list.filter {
                (it as Expositions).commune.lowercase().contains(commune.lowercase())
            }

            Contenus::class -> list.filter {
                (it as Contenus).commune.lowercase().contains(commune.lowercase())
            }

            Edifices::class -> list.filter {
                (it as Edifices).commune.lowercase().contains(commune.lowercase())
            }

            Jardins::class -> list.filter {
                (it as Jardins).commune.lowercase().contains(commune.lowercase())
            }

            Festivals::class -> list.filter {
                (it as Festivals).commune.lowercase().contains(commune.lowercase())
            }

            EquipementsSport::class -> list.filter {
                (it as EquipementsSport).commune.lowercase().contains(commune.lowercase())
            }

            Associations::class -> list.filter {
                (it as Associations).commune.lowercase().contains(commune.lowercase())
            }

            else -> list
        }
    }

    /**
     * Trier la liste par nom (si le critère nom est présent)
     */
    fun <T> trierParNom(list: List<T>): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Musees::class -> list.sortedBy { (it as Musees).nom }
            Expositions::class -> list.sortedBy { (it as Expositions).nom }
            Contenus::class -> list.sortedBy { (it as Contenus).nom }
            Edifices::class -> list.sortedBy { (it as Edifices).nom }
            Jardins::class -> list.sortedBy { (it as Jardins).nom }
            Festivals::class -> list.sortedBy { (it as Festivals).nom }
            EquipementsSport::class -> list.sortedBy { (it as EquipementsSport).nom }
            Associations::class -> list.sortedBy { (it as Associations).nom }
            else -> list
        }
    }

    /**
     * Sélectionner les éléments par nom (si le critère nom est présent)
     */
    fun <T> selectionnerParNom(list: List<T>, nom: String): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Musees::class -> list.filter {
                (it as Musees).nom.lowercase().contains(nom.lowercase())
            }

            Expositions::class -> list.filter {
                (it as Expositions).nom.lowercase().contains(nom.lowercase())
            }

            Contenus::class -> list.filter {
                (it as Contenus).nom.lowercase().contains(nom.lowercase())
            }

            Edifices::class -> list.filter {
                (it as Edifices).nom.lowercase().contains(nom.lowercase())
            }

            Jardins::class -> list.filter {
                (it as Jardins).nom.lowercase().contains(nom.lowercase())
            }

            Festivals::class -> list.filter {
                (it as Festivals).nom.lowercase().contains(nom.lowercase())
            }

            EquipementsSport::class -> list.filter {
                (it as EquipementsSport).nom.lowercase().contains(nom.lowercase())
            }

            Associations::class -> list.filter {
                (it as Associations).nom.lowercase().contains(nom.lowercase())
            }

            else -> list
        }
    }

    /**
     * Trier la liste par lieu (si le critère lieu est présent)
     */
    fun <T> trierParLieu(list: List<T>): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Musees::class -> list.sortedBy { (it as Musees).lieu }
            Contenus::class -> list.sortedBy { (it as Contenus).lieu }
            else -> list
        }
    }

    /**
     * Sélectionner les éléments par lieu (si le critère lieu est présent)
     */
    fun <T> selectionnerParLieu(list: List<T>, lieu: String): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Musees::class -> list.filter {
                (it as Musees).lieu.lowercase().contains(lieu.lowercase())
            }

            Contenus::class -> list.filter {
                (it as Contenus).lieu.lowercase().contains(lieu.lowercase())
            }

            else -> list
        }
    }

    /**
     * Trier la liste par code postal (si le critère code postal est présent)
     */
    fun <T> trierParCodePostal(list: List<T>): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Musees::class -> list.sortedBy { (it as Musees).codePostal }
            Contenus::class -> list.sortedBy { (it as Contenus).codePostal }
            Jardins::class -> list.sortedBy { (it as Jardins).codePostal }
            Festivals::class -> list.sortedBy { (it as Festivals).codePostal }
            EquipementsSport::class -> list.sortedBy { (it as EquipementsSport).codePostal }
            Associations::class -> list.sortedBy { (it as Associations).codePostal }
            else -> list
        }
    }

    /**
     * Sélectionner les éléments par code postal (si le critère code postal est présent)
     */
    fun <T> selectionnerParCodePostal(list: List<T>, codePostal: String): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Musees::class -> list.filter {
                (it as Musees).codePostal.lowercase().contains(codePostal.lowercase())
            }

            Contenus::class -> list.filter {
                (it as Contenus).codePostal.lowercase().contains(codePostal.lowercase())
            }

            Jardins::class -> list.filter {
                (it as Jardins).codePostal.lowercase().contains(codePostal.lowercase())
            }

            Festivals::class -> list.filter {
                (it as Festivals).codePostal.lowercase().contains(codePostal.lowercase())
            }

            EquipementsSport::class -> list.filter {
                (it as EquipementsSport).codePostal.lowercase().contains(codePostal.lowercase())
            }

            Associations::class -> list.filter {
                (it as Associations).codePostal.lowercase().contains(codePostal.lowercase())
            }

            else -> list
        }
    }

    /**
     * Sélectionner les éléments par accessibilité (si le critère accessibilité est présent)
     */
    fun <T> selectionnerParAccessible(list: List<T>, accessible: Boolean): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Musees::class -> list.filter { (it as Musees).accessible == accessible }
            Sites::class -> list.filter { (it as Sites).accessible == accessible }
            Expositions::class -> list.filter { (it as Expositions).accessible == accessible }
            Contenus::class -> list.filter { (it as Contenus).accessible == accessible }
            Edifices::class -> list.filter { (it as Edifices).accessible == accessible }
            Jardins::class -> list.filter { (it as Jardins).accessible == accessible }
            Festivals::class -> list.filter { (it as Festivals).accessible == accessible }
            EquipementsSport::class -> list.filter { (it as EquipementsSport).accessible == accessible }
            Associations::class -> list.filter { (it as Associations).accessible == accessible }
            else -> list
        }
    }

    /**
     * Trier la liste par identifiant (si le critère identifiant est présent)
     */
    fun <T> trierParIdentifiant(list: List<T>): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Musees::class -> list.sortedBy { (it as Musees).identifiant }
            Expositions::class -> list.sortedBy { (it as Expositions).identifiant }
            Contenus::class -> list.sortedBy { (it as Contenus).identifiant }
            Associations::class -> list.sortedBy { (it as Associations).identifiant }
            else -> list
        }
    }

    /**
     * Sélectionner par passion
     */
    private fun selectionnerParPassion(
        list: List<AbstractActivity>,
        passion: String
    ): List<AbstractActivity> {
        // passion dans la liste des passions
        return list.filter { it.passions.contains(passion.lowercase()) }
    }

    /**
     * Obtenir la localisation d'une commune
     */

    suspend fun getCoordinates(commune: String): Pair<Double, Double>? {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient().newBuilder().connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).build()
            val url = "https://nominatim.openstreetmap.org/search?q=${
                commune.replace(
                    " ",
                    "+"
                )
            }&format=json&addressdetails=1"
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            Log.d(TAG, "getCoordinates: commune = $commune")

            if (response.isSuccessful) {
                response.body?.string()?.let { responseBody ->
                    val jsonArray = JSONArray(responseBody)
                    if (jsonArray.length() > 0) {
                        val location = jsonArray.getJSONObject(0)
                        val latitude = location.getDouble("lat")
                        val longitude = location.getDouble("lon")
                        return@withContext Pair(latitude, longitude)
                    }
                }
            }
            return@withContext null
        }
    }

    /**
     * Sélectionner par distance (km)
     */
    suspend fun selectionnerParDistance(
        list: List<AbstractActivity>,
        distanceMax: Int,
        localisation: Location
    ): List<AbstractActivity> {
        if (list.isEmpty() || localisation.latitude == 0.0 || localisation.longitude == 0.0) {
            return emptyList()
        }

        val resultList = mutableListOf<AbstractActivity>()

        for (activity in list) {
            val coordinates = getCoordinates(activity.commune)
            Log.d(TAG, "selectionnerParDistance: coordinates = $coordinates)")
            if (coordinates != null) {
                val latitude = coordinates.first
                val longitude = coordinates.second
                val distance = FloatArray(1)
                Location.distanceBetween(
                    localisation.latitude,
                    localisation.longitude,
                    latitude,
                    longitude,
                    distance
                )
                if (distance[0] <= distanceMax * 1000) {
                    resultList.add(activity)
                }
            }
        }
        return resultList
    }

    /**
     * Obtenir les résultats de la recherche
     */
    fun getResultats(app: ChatBot): List<AbstractActivity> {
        val user = app.currentUser
        // Différents critères :
        // Ville, date, distance, type, localisation, passions
        // Récupérer les activités correspondant aux critères
        var list: List<List<AbstractActivity>> = listOf(
            museesList,
            sitesList,
            expositionsList,
            contenusList,
            edificesList,
            jardinsList,
            festivalsList,
            equipementsSportList,
            associationsList
        )
        list.forEach { Log.d(TAG, "getResultats: list = ${it.size}") }
        // Tri par Type
        user.getTypes().forEach { type ->
            list = list.map {
                Log.d(TAG, "getResultats: list = ${it.subList(0, 1)}")
                selectionnerParType(it, type)
            }
        }
        // Afficher le nombre d'éléments de chaque liste
        list.forEach { abstractActivityList ->
            if (abstractActivityList.isNotEmpty()) {
                Log.d(TAG, "getResultats: list = ${abstractActivityList.size}")
            }
        }
        list = list.filter(List<AbstractActivity>::isNotEmpty)
        // Tri par Ville
        user.getVilles().forEach { ville ->
            list = list.map {
                selectionnerParCommune(it, ville)
            }
                .filter(List<AbstractActivity>::isNotEmpty)
        }
        // Tri par Date
        if (date != "null") {
            // TODO : date des activités
        }
        list = list.filter(List<AbstractActivity>::isNotEmpty)
        // Tri par Distance
        if (distance != 0) {
            // TODO : distance des activités à la localisation actuelle
        }
        list = list.filter(List<AbstractActivity>::isNotEmpty)

        // Tri par Localisation
        //val localisation =
        //if (localisation.latitude != 0.0 && localisation.longitude != 0.0) {
        // TODO : activités dans un rayon de 5km par rapport à la localisation actuelle
        //list = runBlocking { list.map { selectionnerParDistance(it, 10, getLocation()) } }
        list = list
            .filter(List<AbstractActivity>::isNotEmpty)
        //}
        // Tri par Passion
        user.getPassions().forEach { passion ->
            list = list
                .map { selectionnerParPassion(it, passion) }
                .filter(List<AbstractActivity>::isNotEmpty)
        }

        list = list.map(::trierParNom)
        // TODO : Trier la liste totale avant de retourner
        Log.d(TAG, "getResultats: Fin de la recherche")
        list.forEach { Log.d(TAG, "getResultats: list = ${it.size}") }
        return list.flatten()
    }

    private fun selectionnerParType(
        it: List<AbstractActivity>,
        type: Type
    ): List<AbstractActivity> {
        Log.d(TAG, "selectionnerParType: filtre = $type")
        return when (type) {
            Type.SPORT -> it.filter {
                it is EquipementsSport || (it is Associations && it.nom.lowercase()
                    .contains("sport"))
            }

            Type.CULTURE -> it.filter { it is Musees || it is Sites || it is Expositions || it is Contenus || it is Edifices || it is Jardins || it is Festivals }
            Type.MUSIQUE -> it.filter {
                it is Festivals && it.discipline.lowercase().contains("musique")
            }

            Type.CINEMA -> it.filter {
                it is Festivals && (it.discipline.lowercase()
                    .contains("cinéma") || it.discipline.lowercase().contains("cinema"))
            }

            Type.LITTERATURE -> it.filter {
                (it is Festivals && (it.discipline.lowercase()
                    .contains("littérature") || it.discipline.lowercase()
                    .contains("litterature") || it.nom.lowercase()
                    .contains("livre") || it.nom.lowercase()
                    .contains("livre")) || (it is Contenus && (it.nom.lowercase()
                    .contains("livre") || it.nom.lowercase()
                    .contains("littérature") || it.nom.lowercase()
                    .contains("litterature")))) || (it is Associations && (it.nom.lowercase()
                    .contains("littérature") || it.nom.lowercase()
                    .contains("litterature") || it.nom.lowercase()
                    .contains("livre")))
            }

            Type.ASSOCIATION -> it.filterIsInstance<Associations>()
            Type.ALL -> it
            Type.AUTRE -> it
        }
    }
}
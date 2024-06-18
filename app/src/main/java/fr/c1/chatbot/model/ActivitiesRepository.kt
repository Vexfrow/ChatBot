package fr.c1.chatbot.model

import fr.c1.chatbot.ChatBot
import fr.c1.chatbot.R
import fr.c1.chatbot.model.activity.AbstractActivity
import fr.c1.chatbot.model.activity.Association
import fr.c1.chatbot.model.activity.Building
import fr.c1.chatbot.model.activity.Content
import fr.c1.chatbot.model.activity.Exposition
import fr.c1.chatbot.model.activity.Festival
import fr.c1.chatbot.model.activity.Garden
import fr.c1.chatbot.model.activity.Museum
import fr.c1.chatbot.model.activity.Site
import fr.c1.chatbot.model.activity.SportEquipment
import fr.c1.chatbot.model.activity.Type
import fr.c1.chatbot.utils.parseCsv
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import android.app.Application
import android.location.Location
import android.util.Log
import java.io.BufferedInputStream
import java.io.InputStream
import java.util.Locale
import java.util.concurrent.TimeUnit

private const val TAG = "ActivitiesRepository"

/**
 * Repository of activities
 */
class ActivitiesRepository {
    companion object {
        /**
         * Interests list
         */
        val passionList: Set<String>
            get() = Association.passions union Content.passions union Site.passions union Museum.passions union Garden.passions union Festival.passions union Exposition.passions union SportEquipment.passions union Building.passions
    }

    /**
     * Cities list
     */
    private val cityList = sortedSetOf<String>()
    val cities: Set<String> get() = cityList

    /**
     * Date choosen by the user
     */
    var date: String = null.toString()

    /**
     * Distance choosen by the user (in km)
     * Default value is 10 km
     */
    var distance = 10

    /**
     * Location of the terminal
     */
    var location: Location = Location("")

    /**
     * Museums list
     */
    private val museumList = mutableListOf<Museum>()
    val museums: List<Museum> get() = museumList

    /**
     * Patrimonials sites list
     */
    private val siteList = mutableListOf<Site>()
    val sites: List<Site> get() = siteList

    /**
     * Expositions list
     */
    private val expositionList = mutableListOf<Exposition>()
    val expositions: List<Exposition> get() = expositionList

    /**
     * Cultural contents list
     */
    private val contentList = mutableListOf<Content>()
    val contents: List<Content> get() = contentList

    /**
     * Buildings with contemporary design list
     */
    private val buildingList = mutableListOf<Building>()
    val buildings: List<Building> get() = buildingList

    /**
     * Remarkable gardens list
     */
    private val gardenList = mutableListOf<Garden>()
    val gardens: List<Garden> get() = gardenList

    /**
     * Festivals list
     */
    private val festivalList = mutableListOf<Festival>()
    val festivals: List<Festival> get() = festivalList

    /**
     * Sport equipments list
     */
    private val sportEquipmentList = mutableListOf<SportEquipment>()
    val sportEquipments: List<SportEquipment> get() = sportEquipmentList

    /**
     * Associations list
     */
    private val associationList = mutableListOf<Association>()

    val associations: List<Association> get() = associationList

    val all: List<AbstractActivity>
        get() = museumList + siteList + expositionList + contentList + buildingList + gardenList + festivalList + sportEquipmentList + associationList

    /**
     * Add a city to the list
     */
    private fun addCity(str: String) {
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

        if (!cityList.contains(tmp))
            cityList.add(tmp)
    }

    /**
     * Initialise the museums list
     */
    fun initMuseums(app: Application) {
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
            val activity = Museum(
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
            museumList.add(activity)
            addCity(commune)
        }
    }

    /**
     * Initialise the patrimonial sites list
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
            val activity = Site(
                region,
                departement,
                commune,
                true,
                latitude,
                longitude
            )
            siteList.add(activity)
            addCity(commune)
        }
    }

    /**
     * Initialise the expositions list
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
            val activity = Exposition(
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
            expositionList.add(activity)
            addCity(commune)
        }
    }

    /**
     * Initialise the cultural contents list
     */
    fun initContents(app: Application) {
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
            val activity = Content(
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
            contentList.add(activity)
            addCity(commune)
        }
    }

    /**
     * Initialise the buildings list
     */
    fun initBuildings(app: Application) {
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
            val activity = Building(
                region,
                departement,
                commune,
                nom,
                adresse,
                true,
                latitude,
                longitude
            )
            buildingList.add(activity)
            addCity(commune)
        }
    }

    /**
     * Initialise the remarkable gardens list
     */
    fun initGardens(app: Application) {
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
            val activity = Garden(
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
            gardenList.add(activity)
            addCity(commune)
        }
    }

    /**
     * Initialise the festivals list
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
            val activity = Festival(
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
            festivalList.add(activity)
            addCity(commune)
        }
    }

    /**
     * Initialise the sport equipments list
     */
    fun initSportEquipments(app: Application) {
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
            val activity = SportEquipment(
                departement,
                commune,
                nom,
                adresse,
                codePostal,
                true,
                latitude,
                longitude
            )
            sportEquipmentList.add(activity)
            addCity(commune)
        }
    }

    /**
     * Initialise the associations list
     */
    fun initAssociations(app: Application) {
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
            val activity = Association(
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
            associationList.add(activity)
        }
    }

    /**
     * Initialise all the lists
     */
    fun initAll(app: Application) {
        initMuseums(app)
        initSites(app)
        initExpositions(app)
        initContents(app)
        initBuildings(app)
        initGardens(app)
        initFestivals(app)
        initSportEquipments(app)
        initAssociations(app)
    }

    /**
     * Display a list
     */
    fun <T> displayList(list: List<T>) {
        list.forEach { println(it) }
    }

    /**
     * Display all the lists
     */
    fun displayAll() {
        displayMuseum()
        displaySites()
        displayExpositions()
        displayContents()
        displayBuildings()
        displayGardens()
        displayFestivals()
        displaySportEquipments()
        displayAssociations()
    }

    /**
     * Display the museums list
     */
    fun displayMuseum() {
        displayList(museumList)
    }

    /**
     * Display the patrimonial sites list
     */
    fun displaySites() {
        displayList(siteList)
    }

    /**
     * Display the expositions list
     */
    fun displayExpositions() {
        displayList(expositionList)
    }

    /**
     * Display the cultural contents list
     */
    fun displayContents() {
        displayList(contentList)
    }

    /**
     * Display the buildings list
     */
    fun displayBuildings() {
        displayList(buildingList)
    }

    /**
     * Display the remarkable gardens list
     */
    fun displayGardens() {
        displayList(gardenList)
    }

    /**
     * Display the festivals list
     */
    fun displayFestivals() {
        displayList(festivalList)
    }

    /**
     * Display the sport equipments list
     */
    fun displaySportEquipments() {
        displayList(sportEquipmentList)
    }

    /**
     * Display the associations list
     */
    fun displayAssociations() {
        displayList(associationList)
    }

    /**
     * Sort the list by region (if the region criterion is present)
     */
    fun <T> sortByRegion(list: List<T>): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Museum::class -> list.sortedBy { (it as Museum).region }
            Site::class -> list.sortedBy { (it as Site).region }
            Exposition::class -> list.sortedBy { (it as Exposition).region }
            Building::class -> list.sortedBy { (it as Building).region }
            Garden::class -> list.sortedBy { (it as Garden).region }
            Festival::class -> list.sortedBy { (it as Festival).region }
            else -> list
        }
    }

    /**
     * Select the elements by region (if the region criterion is present)
     */
    fun <T> selectByRegion(list: List<T>, region: String): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Museum::class -> list.filter {
                (it as Museum).region.lowercase().contains(region.lowercase())
            }

            Site::class -> list.filter {
                (it as Site).region.lowercase().contains(region.lowercase())
            }

            Exposition::class -> list.filter {
                (it as Exposition).region.lowercase().contains(region.lowercase())
            }

            Building::class -> list.filter {
                (it as Building).region.lowercase().contains(region.lowercase())
            }

            Garden::class -> list.filter {
                (it as Garden).region.lowercase().contains(region.lowercase())
            }

            Festival::class -> list.filter {
                (it as Festival).region.lowercase().contains(region.lowercase())
            }

            else -> list
        }
    }

    /**
     * Sort the list by department (if the department criterion is present)
     */
    fun <T> sortByDepartement(list: List<T>): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Museum::class -> list.sortedBy { (it as Museum).department }
            Site::class -> list.sortedBy { (it as Site).department }
            Exposition::class -> list.sortedBy { (it as Exposition).department }
            Building::class -> list.sortedBy { (it as Building).department }
            Garden::class -> list.sortedBy { (it as Garden).department }
            Festival::class -> list.sortedBy { (it as Festival).department }
            SportEquipment::class -> list.sortedBy { (it as SportEquipment).department }
            Association::class -> list.sortedBy { (it as Association).department }
            else -> list
        }
    }

    /**
     * Select the elements by department (if the department criterion is present)
     */
    fun <T> selectByDepartement(list: List<T>, departement: String): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Museum::class -> list.filter {
                (it as Museum).department.lowercase().contains(departement.lowercase())
            }

            Site::class -> list.filter {
                (it as Site).department.lowercase().contains(departement.lowercase())
            }

            Exposition::class -> list.filter {
                (it as Exposition).department.lowercase().contains(departement.lowercase())
            }

            Building::class -> list.filter {
                (it as Building).department.lowercase().contains(departement.lowercase())
            }

            Garden::class -> list.filter {
                (it as Garden).department.lowercase().contains(departement.lowercase())
            }

            Festival::class -> list.filter {
                (it as Festival).department.lowercase().contains(departement.lowercase())
            }

            SportEquipment::class -> list.filter {
                (it as SportEquipment).department.lowercase().contains(departement.lowercase())
            }

            Association::class -> list.filter {
                (it as Association).department.lowercase().contains(departement.lowercase())
            }

            else -> list
        }
    }

    /**
     * Sort the list by commune (if the commune criterion is present)
     */
    fun <T> sortByCommune(list: List<T>): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Museum::class -> list.sortedBy { (it as Museum).commune }
            Site::class -> list.sortedBy { (it as Site).commune }
            Exposition::class -> list.sortedBy { (it as Exposition).commune }
            Content::class -> list.sortedBy { (it as Content).commune }
            Building::class -> list.sortedBy { (it as Building).commune }
            Garden::class -> list.sortedBy { (it as Garden).commune }
            Festival::class -> list.sortedBy { (it as Festival).commune }
            SportEquipment::class -> list.sortedBy { (it as SportEquipment).commune }
            Association::class -> list.sortedBy { (it as Association).commune }
            else -> list
        }
    }

    /**
     * Select the elements by commune (if the commune criterion is present)
     */
    fun <T> selectByCommune(list: List<T>, commune: String): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Museum::class -> list.filter {
                (it as Museum).commune.lowercase().contains(commune.lowercase())
            }

            Site::class -> list.filter {
                (it as Site).commune.lowercase().contains(commune.lowercase())
            }

            Exposition::class -> list.filter {
                (it as Exposition).commune.lowercase().contains(commune.lowercase())
            }

            Content::class -> list.filter {
                (it as Content).commune.lowercase().contains(commune.lowercase())
            }

            Building::class -> list.filter {
                (it as Building).commune.lowercase().contains(commune.lowercase())
            }

            Garden::class -> list.filter {
                (it as Garden).commune.lowercase().contains(commune.lowercase())
            }

            Festival::class -> list.filter {
                (it as Festival).commune.lowercase().contains(commune.lowercase())
            }

            SportEquipment::class -> list.filter {
                (it as SportEquipment).commune.lowercase().contains(commune.lowercase())
            }

            Association::class -> list.filter {
                (it as Association).commune.lowercase().contains(commune.lowercase())
            }

            else -> list
        }
    }

    /**
     * Sort the list by name (if the name criterion is present)
     */
    fun <T> sortByName(list: List<T>): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Museum::class -> list.sortedBy { (it as Museum).name }
            Exposition::class -> list.sortedBy { (it as Exposition).name }
            Content::class -> list.sortedBy { (it as Content).name }
            Building::class -> list.sortedBy { (it as Building).name }
            Garden::class -> list.sortedBy { (it as Garden).name }
            Festival::class -> list.sortedBy { (it as Festival).name }
            SportEquipment::class -> list.sortedBy { (it as SportEquipment).name }
            Association::class -> list.sortedBy { (it as Association).name }
            else -> list
        }
    }

    /**
     * Select the elements by name (if the name criterion is present)
     */
    fun <T> selectByName(list: List<T>, nom: String): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Museum::class -> list.filter {
                (it as Museum).name.lowercase().contains(nom.lowercase())
            }

            Exposition::class -> list.filter {
                (it as Exposition).name.lowercase().contains(nom.lowercase())
            }

            Content::class -> list.filter {
                (it as Content).name.lowercase().contains(nom.lowercase())
            }

            Building::class -> list.filter {
                (it as Building).name.lowercase().contains(nom.lowercase())
            }

            Garden::class -> list.filter {
                (it as Garden).name.lowercase().contains(nom.lowercase())
            }

            Festival::class -> list.filter {
                (it as Festival).name.lowercase().contains(nom.lowercase())
            }

            SportEquipment::class -> list.filter {
                (it as SportEquipment).name.lowercase().contains(nom.lowercase())
            }

            Association::class -> list.filter {
                (it as Association).name.lowercase().contains(nom.lowercase())
            }

            else -> list
        }
    }

    /**
     * Sort the list by location (if the location criterion is present)
     */
    fun <T> sortByLocation(list: List<T>): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Museum::class -> list.sortedBy { (it as Museum).location }
            Content::class -> list.sortedBy { (it as Content).location }
            else -> list
        }
    }

    /**
     * Select the elements by location (if the location criterion is present)
     */
    fun <T> selectByLocation(list: List<T>, lieu: String): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Museum::class -> list.filter {
                (it as Museum).location.lowercase().contains(lieu.lowercase())
            }

            Content::class -> list.filter {
                (it as Content).location.lowercase().contains(lieu.lowercase())
            }

            else -> list
        }
    }

    /**
     * Sort the list by postal code (if the postal code criterion is present)
     */
    fun <T> sortByPostalCode(list: List<T>): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Museum::class -> list.sortedBy { (it as Museum).postalCode }
            Content::class -> list.sortedBy { (it as Content).postalCode }
            Garden::class -> list.sortedBy { (it as Garden).postalCode }
            Festival::class -> list.sortedBy { (it as Festival).postalCode }
            SportEquipment::class -> list.sortedBy { (it as SportEquipment).postalCode }
            Association::class -> list.sortedBy { (it as Association).postalCode }
            else -> list
        }
    }

    /**
     * Select the elements by postal code (if the postal code criterion is present)
     */
    fun <T> selectByPostalCode(list: List<T>, codePostal: String): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Museum::class -> list.filter {
                (it as Museum).postalCode.lowercase().contains(codePostal.lowercase())
            }

            Content::class -> list.filter {
                (it as Content).postalCode.lowercase().contains(codePostal.lowercase())
            }

            Garden::class -> list.filter {
                (it as Garden).postalCode.lowercase().contains(codePostal.lowercase())
            }

            Festival::class -> list.filter {
                (it as Festival).postalCode.lowercase().contains(codePostal.lowercase())
            }

            SportEquipment::class -> list.filter {
                (it as SportEquipment).postalCode.lowercase().contains(codePostal.lowercase())
            }

            Association::class -> list.filter {
                (it as Association).postalCode.lowercase().contains(codePostal.lowercase())
            }

            else -> list
        }
    }

    /**
     * Sort the list by accessibility (if the accessibility criterion is present)
     */
    fun <T> selectByAccessible(list: List<T>, accessible: Boolean): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Museum::class -> list.filter { (it as Museum).accessible == accessible }
            Site::class -> list.filter { (it as Site).accessible == accessible }
            Exposition::class -> list.filter { (it as Exposition).accessible == accessible }
            Content::class -> list.filter { (it as Content).accessible == accessible }
            Building::class -> list.filter { (it as Building).accessible == accessible }
            Garden::class -> list.filter { (it as Garden).accessible == accessible }
            Festival::class -> list.filter { (it as Festival).accessible == accessible }
            SportEquipment::class -> list.filter { (it as SportEquipment).accessible == accessible }
            Association::class -> list.filter { (it as Association).accessible == accessible }
            else -> list
        }
    }

    /**
     * Sort the list by identifiant
     */
    fun <T> sortByIdentifiant(list: List<T>): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Museum::class -> list.sortedBy { (it as Museum).id }
            Exposition::class -> list.sortedBy { (it as Exposition).id }
            Content::class -> list.sortedBy { (it as Content).id }
            Association::class -> list.sortedBy { (it as Association).id }
            else -> list
        }
    }

    /**
     * Select the elements by interest
     */
    private fun selectByPassion(
        list: List<AbstractActivity>,
        passion: String
    ): List<AbstractActivity> {
        // passion dans la liste des passions
        return list.filter { it.passions.contains(passion.lowercase()) }
    }

    /**
     * Get the coordinates of a city
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
     * Select the activities by distance
     */
    suspend fun selectByDistance(
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
     * Get the final result
     */
    fun getResults(app: ChatBot): List<AbstractActivity> {
        val user = app.currentUser
        // Différents critères :
        // Ville, date, distance, type, localisation, passions
        // Récupérer les activités correspondant aux critères
        var list: List<List<AbstractActivity>> = listOf(
            museumList,
            siteList,
            expositionList,
            contentList,
            buildingList,
            gardenList,
            festivalList,
            sportEquipmentList,
            associationList
        )
        list.forEach { Log.d(TAG, "getResultats: list = ${it.size}") }
        // Tri par Type
        user.types.forEach { type ->
            list = list.map {
                Log.d(TAG, "getResultats: list = ${it.subList(0, 1)}")
                selectByType(it, type)
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
        user.cities.forEach { ville ->
            list = list.map {
                selectByCommune(it, ville)
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
        user.passions.forEach { passion ->
            list = list
                .map { selectByPassion(it, passion) }
                .filter(List<AbstractActivity>::isNotEmpty)
        }

        list = list.map(::sortByName)
        // TODO : Trier la liste totale avant de retourner
        Log.d(TAG, "getResultats: Fin de la recherche")
        list.forEach { Log.d(TAG, "getResultats: list = ${it.size}") }
        return list.flatten()
    }

    /**
     * Select the activities by type
     */
    private fun selectByType(
        it: List<AbstractActivity>,
        type: Type
    ): List<AbstractActivity> {
        Log.d(TAG, "selectionnerParType: filtre = $type")
        return when (type) {
            Type.SPORT -> it.filter {
                it is SportEquipment || (it is Association && it.name.lowercase()
                    .contains("sport"))
            }

            Type.CULTURE -> it.filter { it is Museum || it is Site || it is Exposition || it is Content || it is Building || it is Garden || it is Festival }
            Type.MUSIC -> it.filter {
                it is Festival && it.discipline.lowercase().contains("musique")
            }

            Type.CINEMA -> it.filter {
                it is Festival && (it.discipline.lowercase()
                    .contains("cinéma") || it.discipline.lowercase().contains("cinema"))
            }

            Type.LITTERATURE -> it.filter {
                (it is Festival && (it.discipline.lowercase()
                    .contains("littérature") || it.discipline.lowercase()
                    .contains("litterature") || it.name.lowercase()
                    .contains("livre") || it.name.lowercase()
                    .contains("livre")) || (it is Content && (it.name.lowercase()
                    .contains("livre") || it.name.lowercase()
                    .contains("littérature") || it.name.lowercase()
                    .contains("litterature")))) || (it is Association && (it.name.lowercase()
                    .contains("littérature") || it.name.lowercase()
                    .contains("litterature") || it.name.lowercase()
                    .contains("livre")))
            }

            Type.ASSOCIATION -> it.filterIsInstance<Association>()
            Type.ALL -> it
            Type.OTHER -> it
        }
    }
}
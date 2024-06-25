package fr.c1.chatbot.model

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
import android.content.Context
import android.location.Location
import android.util.Log
import java.io.BufferedInputStream
import java.io.InputStream
import java.util.Locale
import java.util.concurrent.TimeUnit

private const val TAG = "ActivitiesRepository"

/**
 * Activities repository
 *
 * @constructor Create Activities repository
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
     * Distance choosen by the user (in km)
     * Default value is 10 km
     */
    var distance = 10 // 10 km par défaut

    /**
     * Location of the terminal
     */
    var location: Location = Location("")

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

        synchronized(cityList) {
            if (!cityList.contains(tmp))
                cityList.add(tmp)
        }
    }

    /**
     * Get museums
     *
     * @param app
     * @return List<Museum>
     */
    fun getMuseums(currentUser: User, ctx: Context): List<Museum> {
        if (currentUser.passions.run { isNotEmpty() && any(Museum.passions::contains) })
            return emptyList()

        // Read CSV file
        val csvIS: InputStream =
            BufferedInputStream(ctx.resources.openRawResource(R.raw.liste_musees_france))

        // Parse CSV file
        return parseCsv(csvIS) { csvRecord ->
            val region = csvRecord[0]
            val departement = csvRecord[1]
            val identifiant = csvRecord[2]
            val commune = csvRecord[3]
            val nom = csvRecord[4]
            val adresse = csvRecord[5]
            val lieu = csvRecord[6]
            val codePostal = csvRecord[7]
            val telephone = csvRecord[8]
            val url = csvRecord[9]
            val latitude = csvRecord[10].toDouble()
            val longitude = csvRecord[11].toDouble()

            addCity(commune)

            Museum(
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
        }
    }

    /**
     * Get sites
     *
     * @param app
     * @return
     */
    fun getSites(currentUser: User, ctx: Context): List<Site> {
        if (currentUser.passions.run { isNotEmpty() && any(Site.passions::contains) })
            return emptyList()

        // Read CSV file
        val csvIS: InputStream =
            BufferedInputStream(ctx.resources.openRawResource(R.raw.liste_sites_patrimoniaux))

        // Parse CSV file
        return parseCsv(csvIS) { csvRecord ->
            val region = csvRecord[1]
            val departement = csvRecord[3]
            val commune = csvRecord[4]
            val latitude = csvRecord[16].toDouble()
            val longitude = csvRecord[17].toDouble()

            addCity(commune)

            Site(
                region,
                departement,
                commune,
                true,
                latitude,
                longitude
            )
        }
    }

    /**
     * Get expositions
     *
     * @param app
     * @return
     */
    fun getExpositions(currentUser: User, ctx: Context): List<Exposition> {
        if (currentUser.passions.run { isNotEmpty() && any(Exposition.passions::contains) })
            return emptyList()

        // Read CSV file
        val csvIS: InputStream =
            BufferedInputStream(ctx.resources.openRawResource(R.raw.liste_expositions))

        // Parse CSV file
        return parseCsv(csvIS) { csvRecord ->
            val region = csvRecord[2]
            val departement = csvRecord[6]
            val commune = csvRecord[3]
            val identifiant = csvRecord[1]
            val nom = csvRecord[4]
            val url = csvRecord[7]
            val (latitude, longitude) = csvRecord[8].split(",")
                .let { it[0].toDouble() to it[1].toDouble() }

            addCity(commune)

            Exposition(
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
        }
    }

    /**
     * Get contents
     *
     * @param app
     * @return List<Content>
     */
    fun getContents(currentUser: User, ctx: Context): List<Content> {
        if (currentUser.passions.run { isNotEmpty() && any(Content.passions::contains) })
            return emptyList()

        // Read CSV file
        val csvIS: InputStream =
            BufferedInputStream(ctx.resources.openRawResource(R.raw.liste_culture))

        // Parse CSV file
        return parseCsv(csvIS) { csvRecord ->
            val identifiant = csvRecord[0]
            val commune = csvRecord[4]
            val nom = csvRecord[1]
            val adresse = csvRecord[2]
            val lieu = csvRecord[6]
            val codePostal = csvRecord[3]
            val url = csvRecord[5]
            val (latitude, longitude) = csvRecord[20].split(",")
                .let { it[0].toDouble() to it[1].toDouble() }

            addCity(commune)

            Content(
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
        }
    }

    /**
     * Get buildings
     *
     * @param app
     * @return List<Building>
     */
    fun getBuildings(currentUser: User, ctx: Context): List<Building> {
        if (currentUser.passions.run { isNotEmpty() && any(Building.passions::contains) })
            return emptyList()

        // Read CSV file
        val csvIS: InputStream =
            BufferedInputStream(ctx.resources.openRawResource(R.raw.liste_edifices_architecture_contemporaine))

        // Parse CSV file
        return parseCsv(csvIS) { csvRecord ->
            val region = csvRecord[1]
            val departement = csvRecord[6]
            val commune = csvRecord[2]
            val adresse = csvRecord[3]
            val nom = csvRecord[5]

            val (latitude, longitude) = csvRecord[4].let {
                if (it.isNotEmpty()) it.split(',').let { c ->
                    c[0].toDouble() to c[1].toDouble()
                }
                else -1.0 to -1.0
            }

            addCity(commune)

            Building(
                region,
                departement,
                commune,
                nom,
                adresse,
                true,
                latitude,
                longitude
            )
        }
    }

    /**
     * Get gardens
     *
     * @param app
     * @return List<Garden>
     */
    fun getGardens(currentUser: User, ctx: Context): List<Garden> {
        if (currentUser.passions.run { isNotEmpty() && any(Garden.passions::contains) })
            return emptyList()

        // Read CSV file
        val csvIS: InputStream =
            BufferedInputStream(ctx.resources.openRawResource(R.raw.liste_jardins_remarquables))

        // Parse CSV file
        return parseCsv(csvIS) { csvRecord ->
            val region = csvRecord[2]
            val departement = csvRecord[3]
            val commune = csvRecord[8]
            val adresse = csvRecord[4]
            val nom = csvRecord[0]
            val codePostal = csvRecord[1]
            val accessible = csvRecord[21].lowercase().contains("ouvert")
            val latitude = csvRecord[12].toDouble()
            val longitude = csvRecord[13].toDouble()

            addCity(commune)

            Garden(
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
        }
    }

    /**
     * Get festivals
     *
     * @param app
     * @return List<Festival>
     */
    fun getFestivals(currentUser: User, ctx: Context): List<Festival> {
        if (currentUser.passions.run { isNotEmpty() && any(Festival.passions::contains) })
            return emptyList()

        // Read CSV file
        val csvIS: InputStream =
            BufferedInputStream(ctx.resources.openRawResource(R.raw.liste_festivals))

        // Parse CSV file
        return parseCsv(csvIS) { csvRecord ->
            val region = csvRecord[2]
            val departement = csvRecord[3]
            val commune = csvRecord[4]
            val adresse = csvRecord[12]
            val nom = csvRecord[0]
            val codePostal = csvRecord[5]
            val discipline = csvRecord[18]
            val (latitude, longitude) = csvRecord[28].let {
                if (it.isNotEmpty()) it.split(',').let { c ->
                    c[0].toDouble() to c[1].toDouble()
                }
                else -1.0 to -1.0
            }

            addCity(commune)

            Festival(
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
        }
    }

    /**
     * Get sport equipments
     *
     * @param app
     * @return List<SportEquipment>
     */
    fun getSportEquipments(currentUser: User, ctx: Context): List<SportEquipment> {
        if (currentUser.passions.run { isNotEmpty() && any(SportEquipment.passions::contains) })
            return emptyList()

        // Read CSV file
        val csvIS: InputStream =
            BufferedInputStream(ctx.resources.openRawResource(R.raw.liste_equipements_sportifs))

        // Parse CSV file
        return parseCsv(csvIS) { csvRecord ->
            val departement = csvRecord[3].substring(0, 2)
            val commune = csvRecord[4]
            val nom = csvRecord[1]
            val adresse = csvRecord[2]
            val codePostal = csvRecord[3]
            val latitude = csvRecord[6].let {
                if (it.isNotEmpty()) it.toDouble()
                else -1.0
            }
            val longitude = csvRecord[7].let {
                if (it.isNotEmpty()) it.toDouble()
                else -1.0
            }

            addCity(commune)
            SportEquipment(
                departement,
                commune,
                nom,
                adresse,
                codePostal,
                true,
                latitude,
                longitude
            )
        }
    }

    /**
     * Get associations
     *
     * @param app
     * @return List<Association>
     */
    fun getAssociations(currentUser: User, ctx: Context): List<Association> {
        if (currentUser.passions.let { it.isNotEmpty() && it.any(Association.passions::contains) })
            return emptyList()

        // Read CSV file
        val csvIS: InputStream =
            BufferedInputStream(ctx.resources.openRawResource(R.raw.liste_asso))

        // Parse CSV file
        return parseCsv(csvIS) { csvRecord ->
            var departement = ""
            if (csvRecord[3].length > 2) {
                departement = csvRecord[3].substring(0, 2)
            }
            val identifiant = csvRecord[0]
            val commune = csvRecord[4]
            val nom = csvRecord[1]
            val adresse = csvRecord[2]
            val codePostal = csvRecord[3]
            val url = csvRecord[5]

            val latitude = csvRecord[6].let {
                if (it.isNotEmpty()) it.toDouble()
                else -1.0
            }
            val longitude = csvRecord[7].let {
                if (it.isNotEmpty()) it.toDouble()
                else -1.0
            }

            addCity(commune)

            Association(
                departement,
                identifiant,
                commune,
                nom,
                adresse,
                codePostal,
                true,
                latitude,
                longitude,
                url
            )
        }
    }

    /**
     * Display list
     *
     * @param list
     */
    fun displayList(list: List<AbstractActivity>) {
        list.forEach { println(it) }
    }

    /**
     * Sort by region
     *
     * @param list
     * @return List<AbstractActivity>
     */
    @Suppress("UNCHECKED_CAST")
    fun sortByRegion(list: List<AbstractActivity>): List<AbstractActivity> = when (list.first()) {
        is Museum -> (list as List<Museum>).sortedBy { it.region }
        is Site -> (list as List<Site>).sortedBy { it.region }
        is Exposition -> (list as List<Exposition>).sortedBy { it.region }
        is Building -> (list as List<Building>).sortedBy { it.region }
        is Garden -> (list as List<Garden>).sortedBy { it.region }
        is Festival -> (list as List<Festival>).sortedBy { it.region }
        else -> list
    }

    /**
     * Select by region
     *
     * @param list
     * @param region
     * @return List<AbstractActivity>
     */
    @Suppress("UNCHECKED_CAST")
    fun selectByRegion(list: List<AbstractActivity>, region: String): List<AbstractActivity> =
        when (list.first()) {
            is Museum -> (list as List<Museum>).filter {
                it.region.lowercase().contains(region.lowercase())
            }

            is Site -> (list as List<Site>).filter {
                it.region.lowercase().contains(region.lowercase())
            }

            is Exposition -> (list as List<Exposition>).filter {
                it.region.lowercase().contains(region.lowercase())
            }

            is Building -> (list as List<Building>).filter {
                it.region.lowercase().contains(region.lowercase())
            }

            is Garden -> (list as List<Garden>).filter {
                it.region.lowercase().contains(region.lowercase())
            }

            is Festival -> (list as List<Festival>).filter {
                it.region.lowercase().contains(region.lowercase())
            }

            else -> list
        }

    /**
     * Sort by departement
     *
     * @param list
     * @return List<AbstractActivity>
     */
    @Suppress("UNCHECKED_CAST")
    fun sortByDepartement(list: List<AbstractActivity>): List<AbstractActivity> =
        when (list.first()) {
            is Museum -> (list as List<Museum>).sortedBy { it.department }
            is Site -> (list as List<Site>).sortedBy { it.department }
            is Exposition -> (list as List<Exposition>).sortedBy { it.department }
            is Building -> (list as List<Building>).sortedBy { it.department }
            is Garden -> (list as List<Garden>).sortedBy { it.department }
            is Festival -> (list as List<Festival>).sortedBy { it.department }
            is SportEquipment -> (list as List<SportEquipment>).sortedBy { it.department }
            is Association -> (list as List<Association>).sortedBy { it.department }
            else -> list
        }

    /**
     * Select by departement
     *
     * @param list
     * @param departement
     * @return List<T>
     */
    @Suppress("UNCHECKED_CAST")
    fun selectByDepartement(
        list: List<AbstractActivity>,
        departement: String
    ): List<AbstractActivity> = when (list.first()) {
        is Museum -> (list as List<Museum>).filter {
            it.department.lowercase().contains(departement.lowercase())
        }

        is Site -> (list as List<Site>).filter {
            it.department.lowercase().contains(departement.lowercase())
        }

        is Exposition -> (list as List<Exposition>).filter {
            it.department.lowercase().contains(departement.lowercase())
        }

        is Building -> (list as List<Building>).filter {
            it.department.lowercase().contains(departement.lowercase())
        }

        is Garden -> (list as List<Garden>).filter {
            it.department.lowercase().contains(departement.lowercase())
        }

        is Festival -> (list as List<Festival>).filter {
            it.department.lowercase().contains(departement.lowercase())
        }

        is SportEquipment -> (list as List<SportEquipment>).filter {
            it.department.lowercase().contains(departement.lowercase())
        }

        is Association -> (list as List<Association>).filter {
            it.department.lowercase().contains(departement.lowercase())
        }

        else -> list
    }

    /**
     * Sort by commune
     *
     * @param list
     * @return List<AbstractActivity>
     */
    @Suppress("UNCHECKED_CAST")
    fun sortByCommune(list: List<AbstractActivity>): List<AbstractActivity> = when (list.first()) {
        is Museum -> (list as List<Museum>).sortedBy { it.commune }
        is Site -> (list as List<Site>).sortedBy { it.commune }
        is Exposition -> (list as List<Exposition>).sortedBy { it.commune }
        is Content -> (list as List<Content>).sortedBy { it.commune }
        is Building -> (list as List<Building>).sortedBy { it.commune }
        is Garden -> (list as List<Garden>).sortedBy { it.commune }
        is Festival -> (list as List<Festival>).sortedBy { it.commune }
        is SportEquipment -> (list as List<SportEquipment>).sortedBy { it.commune }
        is Association -> (list as List<Association>).sortedBy { it.commune }
        else -> list
    }

    /**
     * Select by commune
     *
     * @param list
     * @param commune
     * @return List<AbstractActivity>
     */
    fun selectByCommune(list: List<AbstractActivity>, commune: String): List<AbstractActivity> =
        list.filter { it.commune.equals(commune, true) }

    /**
     * Sort by name
     *
     * @param list
     * @return List<AbstractActivity>
     */
    @Suppress("UNCHECKED_CAST")
    fun sortByName(list: List<AbstractActivity>): List<AbstractActivity> = when (list.first()) {
        is Museum -> (list as List<Museum>).sortedBy { it.name }
        is Exposition -> (list as List<Exposition>).sortedBy { it.name }
        is Content -> (list as List<Content>).sortedBy { it.name }
        is Building -> (list as List<Building>).sortedBy { it.name }
        is Garden -> (list as List<Garden>).sortedBy { it.name }
        is Festival -> (list as List<Festival>).sortedBy { it.name }
        is SportEquipment -> (list as List<SportEquipment>).sortedBy { it.name }
        is Association -> (list as List<Association>).sortedBy { it.name }
        else -> list
    }

    /**
     * Select by name
     *
     * @param list
     * @param nom
     * @return List<AbstractActivity>
     */
    @Suppress("UNCHECKED_CAST")
    fun selectByName(list: List<AbstractActivity>, nom: String): List<AbstractActivity> =
        when (list.first()) {
            is Museum -> (list as List<Museum>).filter {
                it.name.lowercase().contains(nom.lowercase())
            }

            is Exposition -> (list as List<Exposition>).filter {
                it.name.lowercase().contains(nom.lowercase())
            }

            is Content -> (list as List<Content>).filter {
                it.name.lowercase().contains(nom.lowercase())
            }

            is Building -> (list as List<Building>).filter {
                it.name.lowercase().contains(nom.lowercase())
            }

            is Garden -> (list as List<Garden>).filter {
                it.name.lowercase().contains(nom.lowercase())
            }

            is Festival -> (list as List<Festival>).filter {
                it.name.lowercase().contains(nom.lowercase())
            }

            is SportEquipment -> (list as List<SportEquipment>).filter {
                it.name.lowercase().contains(nom.lowercase())
            }

            is Association -> (list as List<Association>).filter {
                it.name.lowercase().contains(nom.lowercase())
            }

            else -> list
        }

    /**
     * Sort by location
     *
     * @param list
     * @return List<AbstractActivity>
     */
    @Suppress("UNCHECKED_CAST")
    fun sortByLocation(list: List<AbstractActivity>): List<AbstractActivity> =
        when (list.first()) {
            is Museum -> (list as List<Museum>).sortedBy { it.location }
            is Content -> (list as List<Content>).sortedBy { it.location }
            else -> list
        }

    /**
     * Select by location
     *
     * @param list
     * @param lieu
     * @return List<AbstractActivity>
     */
    fun selectByLocation(list: List<AbstractActivity>, lieu: String): List<AbstractActivity> =
        when (list.first()) {
            is Museum -> (list as List<Museum>).filter {
                it.location.lowercase().contains(lieu.lowercase())
            }

            is Content -> (list as List<Content>).filter {
                it.location.lowercase().contains(lieu.lowercase())
            }

            else -> list
        }

    /**
     * Sort by postal code
     *
     * @param list
     * @return List<AbstractActivity>
     */
    @Suppress("UNCHECKED_CAST")
    fun sortByPostalCode(list: List<AbstractActivity>): List<AbstractActivity> =
        when (list.first()) {
            is Museum -> (list as List<Museum>).sortedBy { it.postalCode }
            is Content -> (list as List<Content>).sortedBy { it.postalCode }
            is Garden -> (list as List<Garden>).sortedBy { it.postalCode }
            is Festival -> (list as List<Festival>).sortedBy { it.postalCode }
            is SportEquipment -> (list as List<SportEquipment>).sortedBy { it.postalCode }
            is Association -> (list as List<Association>).sortedBy { it.postalCode }
            else -> list
        }

    /**
     * Select by postal code
     *
     * @param list
     * @param codePostal
     * @return List<AbstractActivity>
     */
    @Suppress("UNCHECKED_CAST")
    fun selectByPostalCode(
        list: List<AbstractActivity>,
        codePostal: String
    ): List<AbstractActivity> = when (list.first()) {
        is Museum -> (list as List<Museum>).filter {
            it.postalCode.lowercase().contains(codePostal.lowercase())
        }

        is Content -> (list as List<Content>).filter {
            it.postalCode.lowercase().contains(codePostal.lowercase())
        }

        is Garden -> (list as List<Garden>).filter {
            it.postalCode.lowercase().contains(codePostal.lowercase())
        }

        is Festival -> (list as List<Festival>).filter {
            it.postalCode.lowercase().contains(codePostal.lowercase())
        }

        is SportEquipment -> (list as List<SportEquipment>).filter {
            it.postalCode.lowercase().contains(codePostal.lowercase())
        }

        is Association -> (list as List<Association>).filter {
            it.postalCode.lowercase().contains(codePostal.lowercase())
        }

        else -> list
    }

    /**
     * Select by accessible
     *
     * @param list
     * @param accessible
     * @return List<AbstractActivity>
     */
    @Suppress("UNCHECKED_CAST")
    fun selectByAccessible(
        list: List<AbstractActivity>,
        accessible: Boolean
    ): List<AbstractActivity> = when (list.first()) {
        is Museum -> (list as List<Museum>).filter { it.accessible == accessible }
        is Site -> (list as List<Site>).filter { it.accessible == accessible }
        is Exposition -> (list as List<Exposition>).filter { it.accessible == accessible }
        is Content -> (list as List<Content>).filter { it.accessible == accessible }
        is Building -> (list as List<Building>).filter { it.accessible == accessible }
        is Garden -> (list as List<Garden>).filter { it.accessible == accessible }
        is Festival -> (list as List<Festival>).filter { it.accessible == accessible }
        is SportEquipment -> (list as List<SportEquipment>).filter { it.accessible == accessible }
        is Association -> (list as List<Association>).filter { it.accessible == accessible }
        else -> list
    }

    /**
     * Sort by identifiant
     *
     * @param list
     * @return List<AbstractActivity>
     */
    @Suppress("UNCHECKED_CAST")
    fun sortByIdentifiant(list: List<AbstractActivity>): List<AbstractActivity> =
        when (list.first()) {
            is Museum -> (list as List<Museum>).sortedBy { it.id }
            is Exposition -> (list as List<Exposition>).sortedBy { it.id }
            is Content -> (list as List<Content>).sortedBy { it.id }
            is Association -> (list as List<Association>).sortedBy { it.id }
            else -> list
        }

    /**
     * Select by passion
     *
     * @param list
     * @param passion
     * @return List<AbstractActivity>
     */
    fun selectByPassion(list: List<AbstractActivity>, passion: String): List<AbstractActivity> =
        list.filter { it.passions.contains(passion.lowercase()) }


    suspend fun getCoordinates(commune: String): Pair<Double, Double>? {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient().newBuilder().connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).build()
            val url = "https://nominatim.openstreetmap.org/search?q=${
                commune.replace(" ", "+")
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
     * Select by distance
     *
     * @param list
     * @param distanceMax
     * @return List<AbstractActivity>
     */
    fun selectByDistance(
        list: List<AbstractActivity>,
        distanceMax: Int,
    ): List<AbstractActivity> {
        if (list.isEmpty() || location.latitude == 0.0 || location.longitude == 0.0) {
            return emptyList()
        }

        val resultList = mutableListOf<AbstractActivity>()

        for (activity in list) {
            val distance = FloatArray(1)
            Location.distanceBetween(
                location.latitude,
                location.longitude,
                activity.latitude,
                activity.longitude,
                distance
            )
            if (distance[0] <= distanceMax * 1000) {
                resultList.add(activity)
            }
        }
        return resultList
    }

    /**
     * Select by type
     *
     * @param it
     * @param type
     * @return List<AbstractActivity>
     */
    fun selectByType(
        it: List<AbstractActivity>, type: Type
    ): List<AbstractActivity> {
        Log.d(TAG, "selectionnerParType: filtre = $type")
        return when (type) {
            Type.SPORT -> it.filter {
                it is SportEquipment || (it is Association && it.name.contains("sport", true))
            }

            Type.CULTURE -> it.filter {
                it is Museum || it is Site || it is Exposition || it is Content || it is Building || it is Garden || it is Festival
            }

            Type.MUSIC -> it.filter {
                it is Festival && it.discipline.contains("musique", true)
            }

            Type.CINEMA -> it.filter {
                it is Festival && (it.discipline.contains("cinéma", true) || it.discipline.contains(
                    "cinema",
                    true
                ))
            }

            Type.LITTERATURE -> it.filter {
                it is Festival && (it.discipline.contains(
                    "littérature",
                    true
                ) || it.discipline.contains("litterature", true) || it.name.contains(
                    "livre",
                    true
                ) || it.name.contains(
                    "livre",
                    true
                )) || (it is Content && (it.name.contains(
                    "livre",
                    true
                ) || it.name.contains("littérature", true) || it.name.contains(
                    "litterature",
                    true
                ))) || (it is Association && (it.name.contains(
                    "littérature",
                    true
                ) || it.name.contains("litterature", true) || it.name.contains("livre", true)))
            }

            Type.ASSOCIATION -> it.filterIsInstance<Association>()
            Type.ALL -> it
            Type.OTHER -> it
        }
    }
}
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
    fun sortByRegion(list: List<AbstractActivity>): List<AbstractActivity> {
        val clazz = list.first()::class
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
     * Select by region
     *
     * @param list
     * @param region
     * @return List<AbstractActivity>
     */
    fun selectByRegion(list: List<AbstractActivity>, region: String): List<AbstractActivity> {
        val clazz = list.first()::class
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
     * Sort by departement
     *
     * @param list
     * @return List<AbstractActivity>
     */
    fun sortByDepartement(list: List<AbstractActivity>): List<AbstractActivity> {
        val clazz = list.first()::class
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
     * Select by departement
     *
     * @param list
     * @param departement
     * @return List<T>
     */
    fun selectByDepartement(
        list: List<AbstractActivity>, departement: String
    ): List<AbstractActivity> {
        val clazz = list.first()::class
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
     * Sort by commune
     *
     * @param list
     * @return List<AbstractActivity>
     */
    fun sortByCommune(list: List<AbstractActivity>): List<AbstractActivity> {
        val clazz = list.first()::class
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
    fun sortByName(list: List<AbstractActivity>): List<AbstractActivity> {
        val clazz = list.first()::class
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
     * Select by name
     *
     * @param list
     * @param nom
     * @return List<AbstractActivity>
     */
    fun selectByName(list: List<AbstractActivity>, nom: String): List<AbstractActivity> {
        val clazz = list.first()::class
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
     * Sort by location
     *
     * @param list
     * @return List<AbstractActivity>
     */
    fun sortByLocation(list: List<AbstractActivity>): List<AbstractActivity> {
        val clazz = list.first()::class
        return when (clazz) {
            Museum::class -> list.sortedBy { (it as Museum).location }
            Content::class -> list.sortedBy { (it as Content).location }
            else -> list
        }
    }

    /**
     * Select by location
     *
     * @param list
     * @param lieu
     * @return List<AbstractActivity>
     */
    fun selectByLocation(list: List<AbstractActivity>, lieu: String): List<AbstractActivity> {
        val clazz = list.first()::class
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
     * Sort by postal code
     *
     * @param list
     * @return List<AbstractActivity>
     */
    fun sortByPostalCode(list: List<AbstractActivity>): List<AbstractActivity> {
        val clazz = list.first()::class
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
     * Select by postal code
     *
     * @param list
     * @param codePostal
     * @return List<AbstractActivity>
     */
    fun selectByPostalCode(
        list: List<AbstractActivity>, codePostal: String
    ): List<AbstractActivity> {
        val clazz = list.first()::class
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
     * Select by accessible
     *
     * @param list
     * @param accessible
     * @return List<AbstractActivity>
     */
    fun selectByAccessible(
        list: List<AbstractActivity>, accessible: Boolean
    ): List<AbstractActivity> {
        val clazz = list.first()::class
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
     * Sort by identifiant
     *
     * @param list
     * @return List<AbstractActivity>
     */
    fun sortByIdentifiant(list: List<AbstractActivity>): List<AbstractActivity> {
        val clazz = list.first()::class
        return when (clazz) {
            Museum::class -> list.sortedBy { (it as Museum).id }
            Exposition::class -> list.sortedBy { (it as Exposition).id }
            Content::class -> list.sortedBy { (it as Content).id }
            Association::class -> list.sortedBy { (it as Association).id }
            else -> list
        }
    }

    /**
     * Select by passion
     *
     * @param list
     * @param passion
     * @return List<AbstractActivity>
     */
    fun selectByPassion(
        list: List<AbstractActivity>, passion: String
    ): List<AbstractActivity> {
        // passion dans la liste des passions
        return list.filter { it.passions.contains(passion.lowercase()) }
    }

    /**
     * Get coordinates
     *
     * @param commune
     * @return Pair<Double, Double>?
     */

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
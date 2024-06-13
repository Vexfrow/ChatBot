package fr.c1.chatbot.model

import fr.c1.chatbot.ChatBot
import fr.c1.chatbot.R
import fr.c1.chatbot.model.activity.AbstractActivity
import fr.c1.chatbot.model.activity.Association
import fr.c1.chatbot.model.activity.Content
import fr.c1.chatbot.model.activity.Building
import fr.c1.chatbot.model.activity.SportEquipment
import fr.c1.chatbot.model.activity.Exposition
import fr.c1.chatbot.model.activity.Festival
import fr.c1.chatbot.model.activity.Jardins
import fr.c1.chatbot.model.activity.Musees
import fr.c1.chatbot.model.activity.Sites
import fr.c1.chatbot.model.activity.Type
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

// Fichiers CSV venant du site data.gouv.fr
class ActivitiesRepository {

    private val TAG = "ActivitiesRepository"

    companion object {
        val passionList: Set<String>
            get() = Association.passions union Content.passions union Sites.passions union Musees.passions union Jardins.passions union Festival.passions union Exposition.passions union SportEquipment.passions union Building.passions
    }

    private val listeVillesDisponible = sortedSetOf<String>()
    val cities: Set<String> get() = listeVillesDisponible


    var date: String = null.toString()

    var distance = 10 // 10 km par défaut

    var location: Location = Location("")

    /**
     * Liste des musées
     */
    private val museesList = mutableListOf<Musees>()
    val museums: List<Musees> get() = museesList

    /**
     * Liste des sites patrimoniaux
     */
    private val sitesList = mutableListOf<Sites>()
    val sites: List<Sites> get() = sitesList

    /**
     * Liste des expositions
     */
    private val expositionList = mutableListOf<Exposition>()
    val expositions: List<Exposition> get() = expositionList

    /**
     * Liste des contenus culturels
     */
    private val contenusList = mutableListOf<Content>()
    val contents: List<Content> get() = contenusList

    /**
     * Liste des édifices
     */
    private val buildingList = mutableListOf<Building>()
    val buildings: List<Building> get() = buildingList

    /**
     * Liste des jardins
     */
    private val jardinsList = mutableListOf<Jardins>()
    val gardens: List<Jardins> get() = jardinsList

    /**
     * Liste des festivals
     */
    private val festivalList = mutableListOf<Festival>()
    val festivals: List<Festival> get() = festivalList

    /**
     * Liste des équipements sportifs
     */
    private val sportEquipmentList = mutableListOf<SportEquipment>()
    val sportEquipments: List<SportEquipment> get() = sportEquipmentList

    /**
     * Liste des associations
     */
    private val associationsList = mutableListOf<Association>()

    /**
     * Récupérer la liste des associations
     */
    val associations: List<Association> get() = associationsList

    val all: List<AbstractActivity>
        get() = museesList + sitesList + expositionList + contenusList + buildingList + jardinsList + festivalList + sportEquipmentList + associationsList

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
     * Initialiser les musées
     */
    fun initMusees(app: Application) {
        // Lire le fichier csv
        val csvIS: InputStream =
            BufferedInputStream(app.resources.openRawResource(R.raw.liste_musees_france))
        val csvParser = csvIS.bufferedReader().lineSequence().map { it.split(";") }

        // Créer la liste des sites patrimoniaux
        csvParser.drop(1).forEach { csvRecord ->
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
                true
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
        // code_region;region;code_departement;departement;commune;autres_communes_dans_le_spr;code_insee;population;nombre_de_spr;numero_du_spr;spr_initial_regime_de_creation;spr_initial_date_de_creation;nombre_de_plans;type_de_plan_en_vigueur;evolution_du_type_de_plan;date_du_plan_en_vigueur
        val csvIS: InputStream =
            BufferedInputStream(app.resources.openRawResource(R.raw.liste_sites_patrimoniaux))
        val csvParser = csvIS.bufferedReader().lineSequence().map { it.split(";") }

        // Créer la liste des sites patrimoniaux
        csvParser.drop(1).forEach { csvRecord ->
            val region = csvRecord[1]
            val departement = csvRecord[3]
            val commune = csvRecord[4]
            val activity = Sites(
                region,
                departement,
                commune,
                true
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
        // annee;id_museofile;region;commune;nom_du_musee;titre_de_l_exposition;departement;url;coordonnees
        val csvIS: InputStream =
            BufferedInputStream(app.resources.openRawResource(R.raw.liste_expositions))
        val csvParser = csvIS.bufferedReader().lineSequence().map { it.split(";") }

        // Créer la liste des sites patrimoniaux
        csvParser.drop(1).forEach { csvRecord ->
            val region = csvRecord[2]
            val departement = csvRecord[6]
            val commune = csvRecord[3]
            val identifiant = csvRecord[1]
            val nom = csvRecord[4]
            val url = csvRecord[7]
            val activity = Exposition(
                region,
                departement,
                identifiant,
                commune,
                nom,
                url,
                true
            )
            expositionList.add(activity)
            addVilleDispo(commune)
        }
    }

    /**
     * Initialiser les contenus culturels
     */
    fun initContenus(app: Application) {
        // Lire le fichier csv
        // identifiant_id;nom_de_l_organisme;adresse_de_l_organisme;code_postal;commune;lien_vers_la_ressource;description_des_contenus_et_de_l_experience_proposes_min_200_max_500_caracteres;si_enfants_merci_de_preciser_le_niveau_scolaire;titre_de_la_ressource;activite_proposee_apprendre_se_divertir_s_informer;public_cible;types_de_ressources_proposees;thematiques;contenus_adaptes_aux_types_de_handicap;temps_d_activite_estime_lecture_ecoute_visionnage_jeu;accessibilite_perennite_de_la_ressource;rattachement_de_l_organisme;autre_precisez;si_limite_dans_le_temps_precisez_jusqu_a_quelle_date;autres_precisez;geolocalisation_ban
        val csvIS: InputStream =
            BufferedInputStream(app.resources.openRawResource(R.raw.liste_culture))
        val csvParser = csvIS.bufferedReader().lineSequence().map { it.split(";") }

        // Créer la liste des sites patrimoniaux
        csvParser.drop(1).forEach { csvRecord ->
            val identifiant = csvRecord[0]
            val commune = csvRecord[4]
            val nom = csvRecord[1]
            val adresse = csvRecord[2]
            val lieu = csvRecord[6]
            val codePostal = csvRecord[3]
            val url = csvRecord[5]
            val activity = Content(
                identifiant,
                commune,
                nom,
                adresse,
                lieu,
                codePostal,
                url,
                true
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
        // reference_de_la_notice;ancienne_reference_de_la_notice_renv;cadre_de_l_etude;region;numero_departement;commune;ancien_nom_commune;insee;lieudit;adresse_normalisee;adresse_forme_editoriale;references_cadastrales;coordonnees;titre_courant;departement_en_lettres;vocable_pour_les_edifices_cultuels;denominations;destination_actuelle_de_l_edifice;siecle_de_la_campagne_principale_de_construction;siecle_de_campagne_secondaire_de_construction;datation_de_l_edifice;description_historique;auteur_de_l_edifice;date_de_label;precisions_sur_l_interet;elements_remarquables_dans_l_edifice;observations;statut_juridique_du_proprietaire;etablissement_affectataire_de_l_edifice;auteur_de_la_photographie_autp;liens_externes;acces_memoire_web;materiaux_du_gros_oeuvre;type_de_couverture;partie_d_elevation_exterieure;materiaux_de_la_couverture;typologie_de_plan;technique_du_decor_porte_de_l_edifice;indexation_iconographique_normalisee;description_de_l_elevation_interieure;emplacement_forme_structure_escalier;etat_de_conservation;description_de_l_edifice;commune_forme_editoriale
        val csvIS: InputStream =
            BufferedInputStream(app.resources.openRawResource(R.raw.liste_edifices_architecture_contemporaine))
        val csvParser = csvIS.bufferedReader().lineSequence().map { it.split(";") }

        // Créer la liste des sites patrimoniaux
        csvParser.drop(1).forEach { csvRecord ->
            val region = csvRecord[3]
            val departement = csvRecord[14] + " (" + csvRecord[4] + ")"
            val commune = csvRecord[5]
            val adresse = csvRecord[9]
            val nom = csvRecord[13]
            val activity = Building(
                region,
                departement,
                commune,
                nom,
                adresse,
                true
            )
            buildingList.add(activity)
            addVilleDispo(commune)
        }
    }

    /**
     * Initialiser les jardins
     */
    fun initJardins(app: Application) {
        // Lire le fichier csv
        // nom_du_jardin;code_postal;region;departement;adresse_complete;adresse_de_l_entree_du_public;numero_et_libelle_de_la_voie;complement_d_adresse;commune;code_insee;code_insee_departement;code_insee_region;latitude;longitude;site_internet_et_autres_liens;types;annee_d_obtention;description;auteur_nom_de_l_illustre;identifiant_deps;identifiant_origine;accessible_au_public;conditions_d_ouverture;equipement_precision;date_de_creation;date_de_maj;coordonnees_geographiques
        val csvIS: InputStream =
            BufferedInputStream(app.resources.openRawResource(R.raw.liste_jardins_remarquables))
        val csvParser = csvIS.bufferedReader().lineSequence().map { it.split(";") }

        // Créer la liste des jardins remarquables
        csvParser.drop(1).forEach { csvRecord ->
            val region = csvRecord[2]
            val departement = csvRecord[3]
            val commune = csvRecord[8]
            val adresse = csvRecord[4]
            val nom = csvRecord[0]
            val codePostal = csvRecord[1]
            val accessible = csvRecord[21].lowercase().contains("ouvert")
            val activity = Jardins(
                region,
                departement,
                commune,
                nom,
                adresse,
                codePostal,
                accessible
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
        // Nom du festival;Envergure territoriale;Région principale de déroulement;Département principal de déroulement;Commune principale de déroulement;Code postal (de la commune principale de déroulement);Code Insee commune;Code Insee EPCI;Libellé EPCI;Numéro de voie;Type de voie (rue, Avenue, boulevard, etc.);Nom de la voie;Adresse postale;Complément d'adresse (facultatif);Site internet du festival;Adresse e-mail;Décennie de création du festival;Année de création du festival;Discipline dominante;Sous-catégorie spectacle vivant;Sous-catégorie musique;Sous-catégorie Musique CNM;Sous-catégorie cinéma et audiovisuel;Sous-catégorie arts visuels et arts numériques;Sous-catégorie livre et littérature;Période principale de déroulement du festival;Identifiant Agence A;Identifiant;Géocodage xy;identifiant CNM
        val csvIS: InputStream =
            BufferedInputStream(app.resources.openRawResource(R.raw.liste_festivals))
        val csvParser = csvIS.bufferedReader().lineSequence().map { it.split(";") }

        // Créer la liste des festivals
        csvParser.drop(1).forEach { csvRecord ->
            val region = csvRecord[2]
            val departement = csvRecord[3]
            val commune = csvRecord[4]
            val adresse = csvRecord[12]
            val nom = csvRecord[0]
            val codePostal = csvRecord[5]
            val discipline = csvRecord[18]
            val activity = Festival(
                region,
                departement,
                commune,
                nom,
                adresse,
                codePostal,
                discipline,
                true
            )
            festivalList.add(activity)
            addVilleDispo(commune)
        }
    }

    /**
     * Initialiser les équipements sportifs
     */
    fun initEquipementsSport(app: Application) {
        // Lire le fichier csv
        val csvIS: InputStream =
            BufferedInputStream(app.resources.openRawResource(R.raw.liste_equipements_sportifs))
        val csvParser = csvIS.bufferedReader().lineSequence().map { it.split(";") }

        // Créer la liste des équipements sportifs
        csvParser.drop(1).forEach { csvRecord ->
            val departement = csvRecord[8]
            val identifiant = csvRecord[0]
            val commune = csvRecord[5]
            val nom = csvRecord[1]
            val adresse = csvRecord[2]
            val codePostal = csvRecord[3]
            val url = csvRecord[7]
            val accessible = csvRecord[6].contains("True")
            val activity = SportEquipment(
                departement,
                identifiant,
                commune,
                nom,
                adresse,
                codePostal,
                url,
                accessible
            )
            sportEquipmentList.add(activity)
            addVilleDispo(commune)
        }
    }

    /**
     * Initialiser les associations
     */
    fun initAsso(app: Application) {
        // Lire le fichier csv
        // id, date_creation, titre, adresse1, code_postal, commune
        val csvIS: InputStream =
            BufferedInputStream(app.resources.openRawResource(R.raw.liste_asso))

        val csvParser = csvIS.bufferedReader().lineSequence().map { it.split(",") }

        // Créer la liste des associations
        csvParser.drop(1).forEach { csvRecord ->
            // Checker la taille du csvRecord[4] pour éviter les erreurs
            val departement = if (csvRecord[4].length < 2) "" else csvRecord[4].substring(0, 2)
            val identifiant = csvRecord[0]
            val commune = csvRecord[5]
            val nom = csvRecord[2]
            val adresse = csvRecord[3]
            val codePostal = csvRecord[4]
            val activity = Association(
                departement,
                identifiant,
                commune,
                nom,
                adresse,
                codePostal,
                true
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
        displayList(expositionList)
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
        displayList(buildingList)
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
        displayList(festivalList)
    }

    /**
     * Afficher la liste des équipements sportifs
     */
    fun displayEquipementsSport() {
        displayList(sportEquipmentList)
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
            Exposition::class -> list.sortedBy { (it as Exposition).region }
            Building::class -> list.sortedBy { (it as Building).region }
            Jardins::class -> list.sortedBy { (it as Jardins).region }
            Festival::class -> list.sortedBy { (it as Festival).region }
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

            Exposition::class -> list.filter {
                (it as Exposition).region.lowercase().contains(region.lowercase())
            }

            Building::class -> list.filter {
                (it as Building).region.lowercase().contains(region.lowercase())
            }

            Jardins::class -> list.filter {
                (it as Jardins).region.lowercase().contains(region.lowercase())
            }

            Festival::class -> list.filter {
                (it as Festival).region.lowercase().contains(region.lowercase())
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
            Exposition::class -> list.sortedBy { (it as Exposition).department }
            Building::class -> list.sortedBy { (it as Building).department }
            Jardins::class -> list.sortedBy { (it as Jardins).departement }
            Festival::class -> list.sortedBy { (it as Festival).department }
            SportEquipment::class -> list.sortedBy { (it as SportEquipment).department }
            Association::class -> list.sortedBy { (it as Association).department }
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

            Exposition::class -> list.filter {
                (it as Exposition).department.lowercase().contains(departement.lowercase())
            }

            Building::class -> list.filter {
                (it as Building).department.lowercase().contains(departement.lowercase())
            }

            Jardins::class -> list.filter {
                (it as Jardins).departement.lowercase().contains(departement.lowercase())
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
     * Trier la liste par commune (si le critère commune est présent)
     */
    fun <T> trierParCommune(list: List<T>): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Musees::class -> list.sortedBy { (it as Musees).commune }
            Sites::class -> list.sortedBy { (it as Sites).commune }
            Exposition::class -> list.sortedBy { (it as Exposition).commune }
            Content::class -> list.sortedBy { (it as Content).commune }
            Building::class -> list.sortedBy { (it as Building).commune }
            Jardins::class -> list.sortedBy { (it as Jardins).commune }
            Festival::class -> list.sortedBy { (it as Festival).commune }
            SportEquipment::class -> list.sortedBy { (it as SportEquipment).commune }
            Association::class -> list.sortedBy { (it as Association).commune }
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

            Exposition::class -> list.filter {
                (it as Exposition).commune.lowercase().contains(commune.lowercase())
            }

            Content::class -> list.filter {
                (it as Content).commune.lowercase().contains(commune.lowercase())
            }

            Building::class -> list.filter {
                (it as Building).commune.lowercase().contains(commune.lowercase())
            }

            Jardins::class -> list.filter {
                (it as Jardins).commune.lowercase().contains(commune.lowercase())
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
     * Trier la liste par nom (si le critère nom est présent)
     */
    fun <T> trierParNom(list: List<T>): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Musees::class -> list.sortedBy { (it as Musees).nom }
            Exposition::class -> list.sortedBy { (it as Exposition).name }
            Content::class -> list.sortedBy { (it as Content).name }
            Building::class -> list.sortedBy { (it as Building).name }
            Jardins::class -> list.sortedBy { (it as Jardins).nom }
            Festival::class -> list.sortedBy { (it as Festival).name }
            SportEquipment::class -> list.sortedBy { (it as SportEquipment).name }
            Association::class -> list.sortedBy { (it as Association).name }
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

            Exposition::class -> list.filter {
                (it as Exposition).name.lowercase().contains(nom.lowercase())
            }

            Content::class -> list.filter {
                (it as Content).name.lowercase().contains(nom.lowercase())
            }

            Building::class -> list.filter {
                (it as Building).name.lowercase().contains(nom.lowercase())
            }

            Jardins::class -> list.filter {
                (it as Jardins).nom.lowercase().contains(nom.lowercase())
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
     * Trier la liste par lieu (si le critère lieu est présent)
     */
    fun <T> trierParLieu(list: List<T>): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Musees::class -> list.sortedBy { (it as Musees).lieu }
            Content::class -> list.sortedBy { (it as Content).location }
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

            Content::class -> list.filter {
                (it as Content).location.lowercase().contains(lieu.lowercase())
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
            Content::class -> list.sortedBy { (it as Content).postalCode }
            Jardins::class -> list.sortedBy { (it as Jardins).codePostal }
            Festival::class -> list.sortedBy { (it as Festival).postalCode }
            SportEquipment::class -> list.sortedBy { (it as SportEquipment).postalCode }
            Association::class -> list.sortedBy { (it as Association).postalCode }
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

            Content::class -> list.filter {
                (it as Content).postalCode.lowercase().contains(codePostal.lowercase())
            }

            Jardins::class -> list.filter {
                (it as Jardins).codePostal.lowercase().contains(codePostal.lowercase())
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
     * Sélectionner les éléments par accessibilité (si le critère accessibilité est présent)
     */
    fun <T> selectionnerParAccessible(list: List<T>, accessible: Boolean): List<T> {
        val clazz = list.first()!!::class
        return when (clazz) {
            Musees::class -> list.filter { (it as Musees).accessible == accessible }
            Sites::class -> list.filter { (it as Sites).accessible == accessible }
            Exposition::class -> list.filter { (it as Exposition).accessible == accessible }
            Content::class -> list.filter { (it as Content).accessible == accessible }
            Building::class -> list.filter { (it as Building).accessible == accessible }
            Jardins::class -> list.filter { (it as Jardins).accessible == accessible }
            Festival::class -> list.filter { (it as Festival).accessible == accessible }
            SportEquipment::class -> list.filter { (it as SportEquipment).accessible == accessible }
            Association::class -> list.filter { (it as Association).accessible == accessible }
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
            Exposition::class -> list.sortedBy { (it as Exposition).id }
            Content::class -> list.sortedBy { (it as Content).id }
            SportEquipment::class -> list.sortedBy { (it as SportEquipment).id }
            Association::class -> list.sortedBy { (it as Association).id }
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
            val client = OkHttpClient()
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
            expositionList,
            contenusList,
            buildingList,
            jardinsList,
            festivalList,
            sportEquipmentList,
            associationsList
        )
        list.forEach { Log.d(TAG, "getResultats: list = ${it.size}") }
        // Tri par Type
        user.types.forEach { type ->
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
        user.cities.forEach { ville ->
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
        list = list
            //.map { selectionnerParDistance(it, 10, getLocation()) }
            .filter(List<AbstractActivity>::isNotEmpty)
        //}
        // Tri par Passion
        user.passions.forEach { passion ->
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
                it is SportEquipment || (it is Association && it.name.lowercase()
                    .contains("sport"))
            }

            Type.CULTURE -> it.filter { it is Musees || it is Sites || it is Exposition || it is Content || it is Building || it is Jardins || it is Festival }
            Type.MUSIQUE -> it.filter {
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
            Type.AUTRE -> it
        }
    }
}
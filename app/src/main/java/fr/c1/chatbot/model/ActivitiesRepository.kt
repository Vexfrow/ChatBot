package fr.c1.chatbot.model

import fr.c1.chatbot.ChatBot
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
            get() = Associations.passions union Contenus.passions union Sites.passions union Musees.passions union Jardins.passions union Festivals.passions union Expositions.passions union EquipementsSport.passions union Edifices.passions
    }

    private val listeVillesDisponible = sortedSetOf<String>()
    val cities: Set<String> get() = listeVillesDisponible


    var date: String = null.toString()
        private set

    var distance = 10 // 10 km par défaut
        private set

    var location: Location = Location("")
        private set

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
    private val expositionsList = mutableListOf<Expositions>()
    val expositions: List<Expositions> get() = expositionsList

    /**
     * Liste des contenus culturels
     */
    private val contenusList = mutableListOf<Contenus>()
    val contents: List<Contenus> get() = contenusList

    /**
     * Liste des édifices
     */
    private val edificesList = mutableListOf<Edifices>()
    val buildings: List<Edifices> get() = edificesList

    /**
     * Liste des jardins
     */
    private val jardinsList = mutableListOf<Jardins>()
    val gardens: List<Jardins> get() = jardinsList

    /**
     * Liste des festivals
     */
    private val festivalsList = mutableListOf<Festivals>()
    val festivals: List<Festivals> get() = festivalsList

    /**
     * Liste des équipements sportifs
     */
    private val equipementsSportList = mutableListOf<EquipementsSport>()
    val sportEquipments: List<EquipementsSport> get() = equipementsSportList

    /**
     * Liste des associations
     */
    private val associationsList = mutableListOf<Associations>()

    /**
     * Récupérer la liste des associations
     */
    val associations: List<Associations> get() = associationsList

    val all: List<AbstractActivity>
        get() = museesList + sitesList + expositionsList + contenusList + edificesList + jardinsList + festivalsList + equipementsSportList + associationsList

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
            val activity = Expositions(
                region,
                departement,
                identifiant,
                commune,
                nom,
                url,
                true
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
            val activity = Contenus(
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
            val activity = Edifices(
                region,
                departement,
                commune,
                nom,
                adresse,
                true
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
            val activity = Festivals(
                region,
                departement,
                commune,
                nom,
                adresse,
                codePostal,
                discipline,
                true
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
            val activity = EquipementsSport(
                departement,
                identifiant,
                commune,
                nom,
                adresse,
                codePostal,
                url,
                accessible
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
            val activity = Associations(
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
            EquipementsSport::class -> list.sortedBy { (it as EquipementsSport).identifiant }
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
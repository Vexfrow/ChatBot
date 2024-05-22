package fr.c1.chatbot.model

import android.app.Application
import fr.c1.chatbot.R
import java.io.BufferedInputStream
import java.io.InputStream

// Fichiers CSV venant du site data.gouv.fr
class ActivitiesRepository {
    // Liste des musées
    private val museesList = mutableListOf<Activities>()
    // Liste des sites patrimoniaux
    private val sitesList = mutableListOf<Activities>()
    // Liste des expositions
    private val expositionsList = mutableListOf<Activities>()
    // Liste des contenus culturels
    private val contenusList = mutableListOf<Activities>()
    // Liste des edifices avec architecture remarquable
    private val edificesList = mutableListOf<Activities>()
    // Liste des jardins remarquables
    private val jardinsList = mutableListOf<Activities>()
    // Liste des festivals
    private val festivalsList = mutableListOf<Activities>()
    // Liste des équipements sportifs
    private val equipementsSportList = mutableListOf<Activities>()
    // Liste des associations
    private val associationsList = mutableListOf<Activities>()

    private fun init(InputStream: InputStream, List: MutableList<Activities>){
        val csvParser = InputStream.bufferedReader().lineSequence().map { it.split(";") }

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
            val activity = Activities(
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
            List.add(activity)
        }
    }

    fun getMuseesList(): List<Activities> {
        return museesList
    }

    fun getSitesList(): List<Activities> {
        return sitesList
    }

    fun getExpositionsList(): List<Activities> {
        return expositionsList
    }

    fun getContenusList(): List<Activities> {
        return contenusList
    }

    fun getEdificesList(): List<Activities> {
        return edificesList
    }

    fun getJardinsList(): List<Activities> {
        return jardinsList
    }

    fun getFestivalsList(): List<Activities> {
        return festivalsList
    }

    fun getEquipementsSportList(): List<Activities> {
        return equipementsSportList
    }

    fun getAssociationsList(): List<Activities> {
        return associationsList
    }

    fun initMusees(app: Application) {
        // Lire le fichier csv
        val csvIS: InputStream =
            BufferedInputStream(app.resources.openRawResource(R.raw.liste_musees_france))
        init(csvIS, museesList)
    }

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
            val activity = Activities(
                region,
                departement,
                "",
                commune,
                "", "", "", "", "", "",
                true
            )
            sitesList.add(activity)
        }
    }

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
            val activity = Activities(
                region,
                departement,
                identifiant,
                commune,
                nom,
                "", "", "", "",
                url,
                true
            )
            expositionsList.add(activity)
        }
    }

    fun initContenus(app: Application) {
        // Lire le fichier csv
        // identifiant_id;nom_de_l_organisme;adresse_de_l_organisme;code_postal;commune;lien_vers_la_ressource;description_des_contenus_et_de_l_experience_proposes_min_200_max_500_caracteres;si_enfants_merci_de_preciser_le_niveau_scolaire;titre_de_la_ressource;activite_proposee_apprendre_se_divertir_s_informer;public_cible;types_de_ressources_proposees;thematiques;contenus_adaptes_aux_types_de_handicap;temps_d_activite_estime_lecture_ecoute_visionnage_jeu;accessibilite_perennite_de_la_ressource;rattachement_de_l_organisme;autre_precisez;si_limite_dans_le_temps_precisez_jusqu_a_quelle_date;autres_precisez;geolocalisation_ban
        val csvIS: InputStream =
            BufferedInputStream(app.resources.openRawResource(R.raw.liste_culture))
        val csvParser = csvIS.bufferedReader().lineSequence().map { it.split(";") }

        // Créer la liste des sites patrimoniaux
        csvParser.drop(1).forEach { csvRecord ->
            //val region = csvRecord[0]
            //val departement = csvRecord[1]
            val identifiant = csvRecord[0]
            val commune = csvRecord[4]
            val nom = csvRecord[1]
            val adresse = csvRecord[2]
            val lieu = csvRecord[6]
            val codePostal = csvRecord[3]
            //val telephone = csvRecord[8]
            val url = csvRecord[5]
            val activity = Activities(
                "", "",
                identifiant,
                commune,
                nom,
                adresse,
                lieu,
                codePostal,
                "",
                url,
                true
            )
            contenusList.add(activity)
        }
    }

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
            val activity = Activities(
                region,
                departement,
                "",
                commune,
                nom,
                adresse, "", "", "", "",
                true
            )
            edificesList.add(activity)
        }
    }

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
            println(csvRecord[21])
            println("nom $nom")
            val accessible = csvRecord[21].contains("ouvert") || csvRecord[21].contains("Ouvert")
            val activity = Activities(
                region,
                departement,
                "",
                commune,
                nom,
                adresse,
                "",
                codePostal,
                "", "",
                accessible
            )
            jardinsList.add(activity)
        }
    }

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
            val activity = Activities(
                region,
                departement,
                "",
                commune,
                nom,
                adresse,
                "",
                codePostal,
                "", "",
                true
            )
            festivalsList.add(activity)
        }
    }

    fun initEquipementsSport(app: Application) {
        // Lire le fichier csv
        val csvIS: InputStream =
            BufferedInputStream(app.resources.openRawResource(R.raw.liste_equipements_sportifs))
        val csvParser = csvIS.bufferedReader().lineSequence().map { it.split(";") }

        // Créer la liste des équipements sportifs
        csvParser.drop(1).forEach { csvRecord ->
            println(csvRecord[0] + csvRecord[1])
            val departement = csvRecord[8]
            val commune = csvRecord[5]
            val adresse = csvRecord[2]
            val nom = csvRecord[1]
            val codePostal = csvRecord[3]
            val identifiant = csvRecord[0]
            val accessible = csvRecord[6].contains("True")
            val url = csvRecord[7]
            val activity = Activities(
                "",
                departement,
                identifiant,
                commune,
                nom,
                adresse,
                "",
                codePostal,
                "",
                url,
                accessible
            )
            equipementsSportList.add(activity)
        }
    }

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
            val activity = Activities(
                "",
                departement,
                identifiant,
                commune,
                nom,
                adresse,
                "",
                codePostal,
                "", "",
                true
            )
            associationsList.add(activity)
        }
    }

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

    fun displayList(list: List<Activities>) {
        for (activity in list) {
            println(activity)
        }
    }

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

    fun displayMusees() {
        displayList(museesList)
    }

    fun displaySites() {
        displayList(sitesList)
    }

    fun displayExpositions() {
        displayList(expositionsList)
    }

    fun displayContenus() {
        displayList(contenusList)
    }

    fun displayEdifices() {
        displayList(edificesList)
    }

    fun displayJardins() {
        displayList(jardinsList)
    }

    fun displayFestivals() {
        displayList(festivalsList)
    }

    fun displayEquipementsSport() {
        displayList(equipementsSportList)
    }

    fun displayAssociations() {
        displayList(associationsList)
    }

    fun trierParRegion(list: List<Activities>): List<Activities> {
        return list.sortedBy { it.region }
    }

    fun selectionnerParRegion(list: List<Activities>, region: String): List<Activities> {
        return list.filter { it.region.lowercase().contains(region.lowercase()) }
    }

    fun trierParDepartement(list: List<Activities>): List<Activities> {
        return list.sortedBy { it.departement }
    }

    fun selectionnerParDepartement(list: List<Activities>, departement: String): List<Activities> {
        return list.filter { it.departement.lowercase().contains(departement.lowercase()) }
    }

    fun trierParCommune(list: List<Activities>): List<Activities> {
        return list.sortedBy { it.commune }
    }

    fun selectionnerParCommune(list: List<Activities>, commune: String): List<Activities> {
        return list.filter { it.commune.lowercase().contains(commune.lowercase()) }
    }

    fun trierParNom(list: List<Activities>): List<Activities> {
        return list.sortedBy { it.nom }
    }

    fun selectionnerParNom(list: List<Activities>, nom: String): List<Activities> {
        return list.filter { it.nom.lowercase().contains(nom.lowercase()) }
    }

    fun trierParLieu(list: List<Activities>): List<Activities> {
        return list.sortedBy { it.lieu }
    }

    fun selectionnerParLieu(list: List<Activities>, lieu: String): List<Activities> {
        return list.filter { it.lieu.lowercase().contains(lieu.lowercase()) }
    }

    fun trierParCodePostal(list: List<Activities>): List<Activities> {
        return list.sortedBy { it.codePostal }
    }

    fun selectionnerParCodePostal(list: List<Activities>, codePostal: String): List<Activities> {
        return list.filter { it.codePostal == codePostal }
    }

    fun selectionnerParAccessible(list: List<Activities>, accessible: Boolean): List<Activities> {
        return list.filter { it.accessible == accessible }
    }

    fun trierParIdentifiant(list: List<Activities>): List<Activities> {
        return list.sortedBy { it.identifiant }
    }
}
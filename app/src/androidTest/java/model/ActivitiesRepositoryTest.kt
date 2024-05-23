package model

import androidx.test.core.app.ApplicationProvider
import fr.c1.chatbot.ChatBot
import fr.c1.chatbot.model.activity.Musees
import fr.c1.chatbot.model.ActivitiesRepository
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream


class ActivitiesRepositoryTest {
    private lateinit var app: ChatBot
    private lateinit var activitiesRepository: ActivitiesRepository
    private val standardOut: PrintStream = System.out
    private val outputStreamCaptor = ByteArrayOutputStream()

    @Before
    fun setUp() {
        app = ApplicationProvider.getApplicationContext() as ChatBot
        activitiesRepository = ActivitiesRepository()
        System.setOut(PrintStream(outputStreamCaptor));
    }

    @After
    fun tearDown() {
        System.setOut(standardOut)
    }

    @Test
    fun initMuseesTest() {
        activitiesRepository.initMusees(app)
        val musees = activitiesRepository.getMuseesList()
        val sizeExpected = 1222
        Assert.assertEquals(
            "Le nombre de musées attendu est $sizeExpected, la taille de la liste est ${musees.size}",
            sizeExpected,
            musees.size
        )
    }

    @Test
    fun initSitesTest() {
        activitiesRepository.initSites(app)
        val sites = activitiesRepository.getSitesList()
        val sizeExpected = 1134
        Assert.assertEquals(
            "Le nombre de sites attendu est $sizeExpected, la taille de la liste est ${sites.size}",
            sizeExpected,
            sites.size
        )
    }

    @Test
    fun initExpositionsTest() {
        activitiesRepository.initExpositions(app)
        val expositions = activitiesRepository.getExpositionsList()
        val sizeExpected = 243
        Assert.assertEquals(
            "Le nombre d'expositions attendu est $sizeExpected, la taille de la liste est ${expositions.size}",
            sizeExpected,
            expositions.size
        )
    }

    @Test
    fun initContenusTest() {
        activitiesRepository.initContenus(app)
        val contenus = activitiesRepository.getContenusList()
        val sizeExpected = 1108
        Assert.assertEquals(
            "Le nombre de contenus culturels attendu est $sizeExpected, la taille de la liste est ${contenus.size}",
            sizeExpected,
            contenus.size
        )
    }

    @Test
    fun initEdificesTest() {
        activitiesRepository.initEdifices(app)
        val edifices = activitiesRepository.getEdificesList()
        val sizeExpected = 1657
        Assert.assertEquals(
            "Le nombre d'édifices attendu est $sizeExpected, la taille de la liste est ${edifices.size}",
            sizeExpected,
            edifices.size
        )
    }

    @Test
    fun initJardinsTest() {
        activitiesRepository.initJardins(app)
        val jardins = activitiesRepository.getJardinsList()
        val sizeExpected = 413
        Assert.assertEquals(
            "Le nombre de jardins attendu est $sizeExpected, la taille de la liste est ${jardins.size}",
            sizeExpected,
            jardins.size
        )
    }

    @Test
    fun initFestivalsTest() {
        activitiesRepository.initFestivals(app)
        val festivals = activitiesRepository.getFestivalsList()
        val sizeExpected = 7283
        Assert.assertEquals(
            "Le nombre de festivals attendu est $sizeExpected, la taille de la liste est ${festivals.size}",
            sizeExpected,
            festivals.size
        )
    }

    @Test
    fun initEquipementsSportTest() {
        activitiesRepository.initEquipementsSport(app)
        val equipementsSport = activitiesRepository.getEquipementsSportList()
        val sizeExpected = 159463
        Assert.assertEquals(
            "Le nombre d'équipements sportifs attendu est $sizeExpected, la taille de la liste est ${equipementsSport.size}",
            sizeExpected,
            equipementsSport.size
        )
    }

    @Test
    fun initAssociationsTest() {
        activitiesRepository.initAsso(app)
        val asso = activitiesRepository.getAssociationsList()
        val sizeExpected = 23837
        Assert.assertEquals(
            "Le nombre d'associations attendu est $sizeExpected, la taille de la liste est ${asso.size}",
            sizeExpected,
            asso.size
        )
    }

    @Test
    fun initAllTest() {
        activitiesRepository.initAll(app)
        val musees = activitiesRepository.getMuseesList()
        val sites = activitiesRepository.getSitesList()
        val expositions = activitiesRepository.getExpositionsList()
        val contenus = activitiesRepository.getContenusList()
        val edifices = activitiesRepository.getEdificesList()
        val jardins = activitiesRepository.getJardinsList()
        val festivals = activitiesRepository.getFestivalsList()
        val equipementsSport = activitiesRepository.getEquipementsSportList()
        val associations = activitiesRepository.getAssociationsList()
        val sizeExpected = 1222 + 1134 + 243 + 1108 + 1657 + 413 + 7283 + 159463 + 23837
        val sizeActual =
            musees.size + sites.size + expositions.size + contenus.size + edifices.size + jardins.size + festivals.size + equipementsSport.size + associations.size
        Assert.assertEquals(
            "Le nombre total d'activités attendu est $sizeExpected, la taille de la liste est $sizeActual",
            sizeExpected,
            sizeActual
        )
    }

    @Test
    fun getMuseesListTest() {
        activitiesRepository.initMusees(app)
        val musees = activitiesRepository.getMuseesList()
        val sizeExpected = 1222
        Assert.assertEquals(
            "Le nombre de musées attendu est $sizeExpected, la taille de la liste est ${musees.size}",
            sizeExpected,
            musees.size
        )
    }

    @Test
    fun getSitesListTest() {
        activitiesRepository.initSites(app)
        val sites = activitiesRepository.getSitesList()
        val sizeExpected = 1134
        Assert.assertEquals(
            "Le nombre de sites attendu est $sizeExpected, la taille de la liste est ${sites.size}",
            sizeExpected,
            sites.size
        )
    }

    @Test
    fun getExpositionsListTest() {
        activitiesRepository.initExpositions(app)
        val expositions = activitiesRepository.getExpositionsList()
        val sizeExpected = 243
        Assert.assertEquals(
            "Le nombre d'expositions attendu est $sizeExpected, la taille de la liste est ${expositions.size}",
            sizeExpected,
            expositions.size
        )
    }

    @Test
    fun getContenusListTest() {
        activitiesRepository.initContenus(app)
        val contenus = activitiesRepository.getContenusList()
        val sizeExpected = 1108
        Assert.assertEquals(
            "Le nombre de contenus culturels attendu est $sizeExpected, la taille de la liste est ${contenus.size}",
            sizeExpected,
            contenus.size
        )
    }

    @Test
    fun getEdificesListTest() {
        activitiesRepository.initEdifices(app)
        val edifices = activitiesRepository.getEdificesList()
        val sizeExpected = 1657
        Assert.assertEquals(
            "Le nombre d'édifices attendu est $sizeExpected, la taille de la liste est ${edifices.size}",
            sizeExpected,
            edifices.size
        )
    }

    @Test
    fun getJardinsListTest() {
        activitiesRepository.initJardins(app)
        val jardins = activitiesRepository.getJardinsList()
        val sizeExpected = 413
        Assert.assertEquals(
            "Le nombre de jardins attendu est $sizeExpected, la taille de la liste est ${jardins.size}",
            sizeExpected,
            jardins.size
        )
    }

    @Test
    fun getFestivalsListTest() {
        activitiesRepository.initFestivals(app)
        val festivals = activitiesRepository.getFestivalsList()
        val sizeExpected = 7283
        Assert.assertEquals(
            "Le nombre de festivals attendu est $sizeExpected, la taille de la liste est ${festivals.size}",
            sizeExpected,
            festivals.size
        )
    }

    @Test
    fun getEquipementsSportListTest() {
        activitiesRepository.initEquipementsSport(app)
        val equipementsSport = activitiesRepository.getEquipementsSportList()
        val sizeExpected = 159463
        Assert.assertEquals(
            "Le nombre d'équipements sportifs attendu est $sizeExpected, la taille de la liste est ${equipementsSport.size}",
            sizeExpected,
            equipementsSport.size
        )
    }

    @Test
    fun getAssociationsListTest() {
        activitiesRepository.initAsso(app)
        val asso = activitiesRepository.getAssociationsList()
        val sizeExpected = 23837
        Assert.assertEquals(
            "Le nombre d'associations attendu est $sizeExpected, la taille de la liste est ${asso.size}",
            sizeExpected,
            asso.size
        )
    }

    @Test
    fun displayEmptyListTest() {
        // Liste vide
        val emptyList = emptyList<Musees>()
        activitiesRepository.displayList(emptyList)
    }

    @Test
    fun displayListTest() {
        // Liste activities non vide
        val activity1 = Musees(
            "region1",
            "departement1",
            "identifiant1",
            "commune1",
            "nom1",
            "adresse1",
            "lieu1",
            "codePostal1",
            "telephone1",
            "url1",
            true
        )
        val activity2 = Musees(
            "region2",
            "departement2",
            "identifiant2",
            "commune2",
            "nom2",
            "adresse2",
            "lieu2",
            "codePostal2",
            "telephone2",
            "url2",
            false
        )
        val list = listOf(activity1, activity2)
        activitiesRepository.displayList(list)

        val expectedOutput =
            """Activities(region='region1', departement='departement1', identifiant='identifiant1', commune='commune1', nom='nom1', adresse='adresse1', lieu='lieu1', codePostal='codePostal1', telephone='telephone1', url='url1', accessible='true')
            |Activities(region='region2', departement='departement2', identifiant='identifiant2', commune='commune2', nom='nom2', adresse='adresse2', lieu='lieu2', codePostal='codePostal2', telephone='telephone2', url='url2', accessible='false')
        """.trimMargin()
        Assert.assertEquals(expectedOutput, outputStreamCaptor.toString().trim())
    }

    @Test
    fun displayAllTest() {
        activitiesRepository.initAll(app)
        activitiesRepository.displayAll()
        // Ici trop long à tester (vu la longueur des fichiers)
    }

    @Test
    fun trierParRegionTest() {
        val activity1 = Musees(
            "region1",
            "departement1",
            "identifiant1",
            "commune1",
            "nom1",
            "adresse1",
            "lieu1",
            "codePostal1",
            "telephone1",
            "url1",
            true
        )

        val activity2 = Musees(
            "region2",
            "departement2",
            "identifiant2",
            "commune2",
            "nom2",
            "adresse2",
            "lieu2",
            "codePostal2",
            "telephone2",
            "url2",
            false
        )

        val activity3 = Musees(
            "region1",
            "departement3",
            "identifiant3",
            "commune3",
            "nom3",
            "adresse3",
            "lieu3",
            "codePostal3",
            "telephone3",
            "url3",
            true
        )

        val list = listOf(activity1, activity2, activity3)

        val sortedList = activitiesRepository.trierParRegion(list)

        val expectedList = listOf(activity1, activity3, activity2)

        Assert.assertEquals(expectedList, sortedList)
    }

    @Test
    fun selectionnerParRegionTest() {
        val activity1 = Musees(
            "region1",
            "departement1",
            "identifiant1",
            "commune1",
            "nom1",
            "adresse1",
            "lieu1",
            "codePostal1",
            "telephone1",
            "url1",
            true
        )

        val activity2 = Musees(
            "region2",
            "departement2",
            "identifiant2",
            "commune2",
            "nom2",
            "adresse2",
            "lieu2",
            "codePostal2",
            "telephone2",
            "url2",
            false
        )

        val activity3 = Musees(
            "region1",
            "departement3",
            "identifiant3",
            "commune3",
            "nom3",
            "adresse3",
            "lieu3",
            "codePostal3",
            "telephone3",
            "url3",
            true
        )

        val list = listOf(activity1, activity2, activity3)

        val selectedList = activitiesRepository.selectionnerParRegion(list, "region1")

        val expectedList = listOf(activity1, activity3)

        Assert.assertEquals(expectedList, selectedList)
    }

    @Test
    fun trierParDepartementTest() {
        val activity1 = Musees(
            "region1",
            "departement3",
            "identifiant1",
            "commune1",
            "nom1",
            "adresse1",
            "lieu1",
            "codePostal1",
            "telephone1",
            "url1",
            true
        )

        val activity2 = Musees(
            "region2",
            "departement2",
            "identifiant2",
            "commune2",
            "nom2",
            "adresse2",
            "lieu2",
            "codePostal2",
            "telephone2",
            "url2",
            false
        )

        val activity3 = Musees(
            "region1",
            "departement1",
            "identifiant3",
            "commune3",
            "nom3",
            "adresse3",
            "lieu3",
            "codePostal3",
            "telephone3",
            "url3",
            true
        )

        val list = listOf(activity1, activity2, activity3)

        val sortedList = activitiesRepository.trierParDepartement(list)

        val expectedList = listOf(activity3, activity2, activity1)

        Assert.assertEquals(expectedList, sortedList)
    }

    @Test
    fun selectionnerParDepartementTest() {
        val activity1 = Musees(
            "region1",
            "departement3",
            "identifiant1",
            "commune1",
            "nom1",
            "adresse1",
            "lieu1",
            "codePostal1",
            "telephone1",
            "url1",
            true
        )

        val activity2 = Musees(
            "region2",
            "departement2",
            "identifiant2",
            "commune2",
            "nom2",
            "adresse2",
            "lieu2",
            "codePostal2",
            "telephone2",
            "url2",
            false
        )

        val activity3 = Musees(
            "region1",
            "departement1",
            "identifiant3",
            "commune3",
            "nom3",
            "adresse3",
            "lieu3",
            "codePostal3",
            "telephone3",
            "url3",
            true
        )

        val list = listOf(activity1, activity2, activity3)

        val selectedList = activitiesRepository.selectionnerParDepartement(list, "departement3")

        val expectedList = listOf(activity1)

        Assert.assertEquals(expectedList, selectedList)
    }

    @Test
    fun trierParCommuneTest() {
        val activity1 = Musees(
            "region1",
            "departement1",
            "identifiant1",
            "commune3",
            "nom1",
            "adresse1",
            "lieu1",
            "codePostal1",
            "telephone1",
            "url1",
            true
        )

        val activity2 = Musees(
            "region2",
            "departement2",
            "identifiant2",
            "commune2",
            "nom2",
            "adresse2",
            "lieu2",
            "codePostal2",
            "telephone2",
            "url2",
            false
        )

        val activity3 = Musees(
            "region1",
            "departement3",
            "identifiant3",
            "commune1",
            "nom3",
            "adresse3",
            "lieu3",
            "codePostal3",
            "telephone3",
            "url3",
            true
        )

        val list = listOf(activity1, activity2, activity3)

        val sortedList = activitiesRepository.trierParCommune(list)

        val expectedList = listOf(activity3, activity2, activity1)

        Assert.assertEquals(expectedList, sortedList)
    }

    @Test
    fun selectionnerParCommuneTest() {
        val activity1 = Musees(
            "region1",
            "departement1",
            "identifiant1",
            "commune3",
            "nom1",
            "adresse1",
            "lieu1",
            "codePostal1",
            "telephone1",
            "url1",
            true
        )

        val activity2 = Musees(
            "region2",
            "departement2",
            "identifiant2",
            "commune2",
            "nom2",
            "adresse2",
            "lieu2",
            "codePostal2",
            "telephone2",
            "url2",
            false
        )

        val activity3 = Musees(
            "region1",
            "departement3",
            "identifiant3",
            "commune1",
            "nom3",
            "adresse3",
            "lieu3",
            "codePostal3",
            "telephone3",
            "url3",
            true
        )

        val list = listOf(activity1, activity2, activity3)

        val selectedList = activitiesRepository.selectionnerParCommune(list, "commune1")

        val expectedList = listOf(activity3)

        Assert.assertEquals(expectedList, selectedList)
    }

    @Test
    fun trierParNomTest() {
        val activity1 = Musees(
            "region1",
            "departement1",
            "identifiant1",
            "commune3",
            "nom3",
            "adresse1",
            "lieu1",
            "codePostal1",
            "telephone1",
            "url1",
            true
        )

        val activity2 = Musees(
            "region2",
            "departement2",
            "identifiant2",
            "commune2",
            "nom2",
            "adresse2",
            "lieu2",
            "codePostal2",
            "telephone2",
            "url2",
            false
        )

        val activity3 = Musees(
            "region1",
            "departement3",
            "identifiant3",
            "commune1",
            "nom1",
            "adresse3",
            "lieu3",
            "codePostal3",
            "telephone3",
            "url3",
            true
        )

        val list = listOf(activity1, activity2, activity3)

        val sortedList = activitiesRepository.trierParNom(list)

        val expectedList = listOf(activity3, activity2, activity1)

        Assert.assertEquals(expectedList, sortedList)
    }

    @Test
    fun selectionnerParNomTest() {
        val activity1 = Musees(
            "region1",
            "departement1",
            "identifiant1",
            "commune3",
            "nom3",
            "adresse1",
            "lieu1",
            "codePostal1",
            "telephone1",
            "url1",
            true
        )

        val activity2 = Musees(
            "region2",
            "departement2",
            "identifiant2",
            "commune2",
            "nom2",
            "adresse2",
            "lieu2",
            "codePostal2",
            "telephone2",
            "url2",
            false
        )

        val activity3 = Musees(
            "region1",
            "departement3",
            "identifiant3",
            "commune1",
            "nom1",
            "adresse3",
            "lieu3",
            "codePostal3",
            "telephone3",
            "url3",
            true
        )

        val list = listOf(activity1, activity2, activity3)

        val selectedList = activitiesRepository.selectionnerParNom(list, "nom1")

        val expectedList = listOf(activity3)

        Assert.assertEquals(expectedList, selectedList)
    }

    @Test
    fun trierParLieuTest() {
        val activity1 = Musees(
            "region1",
            "departement1",
            "identifiant1",
            "commune3",
            "nom3",
            "adresse1",
            "lieu3",
            "codePostal1",
            "telephone1",
            "url1",
            true
        )

        val activity2 = Musees(
            "region2",
            "departement2",
            "identifiant2",
            "commune2",
            "nom2",
            "adresse2",
            "lieu2",
            "codePostal2",
            "telephone2",
            "url2",
            false
        )

        val activity3 = Musees(
            "region1",
            "departement3",
            "identifiant3",
            "commune1",
            "nom1",
            "adresse3",
            "lieu1",
            "codePostal3",
            "telephone3",
            "url3",
            true
        )

        val list = listOf(activity1, activity2, activity3)

        val sortedList = activitiesRepository.trierParLieu(list)

        val expectedList = listOf(activity3, activity2, activity1)

        Assert.assertEquals(expectedList, sortedList)
    }

    @Test
    fun selectionnerParLieuTest() {
        val activity1 = Musees(
            "region1",
            "departement1",
            "identifiant1",
            "commune3",
            "nom3",
            "adresse1",
            "lieu3",
            "codePostal1",
            "telephone1",
            "url1",
            true
        )

        val activity2 = Musees(
            "region2",
            "departement2",
            "identifiant2",
            "commune2",
            "nom2",
            "adresse2",
            "lieu2",
            "codePostal2",
            "telephone2",
            "url2",
            false
        )

        val activity3 = Musees(
            "region1",
            "departement3",
            "identifiant3",
            "commune1",
            "nom1",
            "adresse3",
            "lieu1",
            "codePostal3",
            "telephone3",
            "url3",
            true
        )

        val list = listOf(activity1, activity2, activity3)

        val selectedList = activitiesRepository.selectionnerParLieu(list, "lieu1")

        val expectedList = listOf(activity3)

        Assert.assertEquals(expectedList, selectedList)
    }

    @Test
    fun trierParCodePostalTest() {
        val activity1 = Musees(
            "region1",
            "departement1",
            "identifiant1",
            "commune3",
            "nom3",
            "adresse1",
            "lieu3",
            "codePostal3",
            "telephone1",
            "url1",
            true
        )

        val activity2 = Musees(
            "region2",
            "departement2",
            "identifiant2",
            "commune2",
            "nom2",
            "adresse2",
            "lieu2",
            "codePostal2",
            "telephone2",
            "url2",
            false
        )

        val activity3 = Musees(
            "region1",
            "departement3",
            "identifiant3",
            "commune1",
            "nom1",
            "adresse3",
            "lieu1",
            "codePostal1",
            "telephone3",
            "url3",
            true
        )

        val list = listOf(activity1, activity2, activity3)

        val sortedList = activitiesRepository.trierParCodePostal(list)

        val expectedList = listOf(activity3, activity2, activity1)

        Assert.assertEquals(expectedList, sortedList)
    }

    @Test
    fun selectionnerParCodePostalTest() {
        val activity1 = Musees(
            "region1",
            "departement1",
            "identifiant1",
            "commune3",
            "nom3",
            "adresse1",
            "lieu3",
            "codePostal3",
            "telephone1",
            "url1",
            true
        )

        val activity2 = Musees(
            "region2",
            "departement2",
            "identifiant2",
            "commune2",
            "nom2",
            "adresse2",
            "lieu2",
            "codePostal2",
            "telephone2",
            "url2",
            false
        )

        val activity3 = Musees(
            "region1",
            "departement3",
            "identifiant3",
            "commune1",
            "nom1",
            "adresse3",
            "lieu1",
            "codePostal1",
            "telephone3",
            "url3",
            true
        )

        val list = listOf(activity1, activity2, activity3)

        val selectedList = activitiesRepository.selectionnerParCodePostal(list, "codePostal1")

        val expectedList = listOf(activity3)

        Assert.assertEquals(expectedList, selectedList)
    }

    @Test
    fun selectionnerParAccessibleTest() {
        val activity1 = Musees(
            "region1",
            "departement1",
            "identifiant1",
            "commune3",
            "nom3",
            "adresse1",
            "lieu3",
            "codePostal3",
            "telephone1",
            "url1",
            true
        )

        val activity2 = Musees(
            "region2",
            "departement2",
            "identifiant2",
            "commune2",
            "nom2",
            "adresse2",
            "lieu2",
            "codePostal2",
            "telephone2",
            "url2",
            false
        )

        val activity3 = Musees(
            "region1",
            "departement3",
            "identifiant3",
            "commune1",
            "nom1",
            "adresse3",
            "lieu1",
            "codePostal1",
            "telephone3",
            "url3",
            true
        )

        val list = listOf(activity1, activity2, activity3)

        val selectedList = activitiesRepository.selectionnerParAccessible(list, true)

        val expectedList = listOf(activity1, activity3)

        Assert.assertEquals(expectedList, selectedList)
    }

    @Test
    fun trierParIdentifiantTest() {
        val activity1 = Musees(
            "region1",
            "departement1",
            "identifiant3",
            "commune3",
            "nom3",
            "adresse1",
            "lieu3",
            "codePostal3",
            "telephone1",
            "url1",
            true
        )

        val activity2 = Musees(
            "region2",
            "departement2",
            "identifiant2",
            "commune2",
            "nom2",
            "adresse2",
            "lieu2",
            "codePostal2",
            "telephone2",
            "url2",
            false
        )

        val activity3 = Musees(
            "region1",
            "departement3",
            "identifiant1",
            "commune1",
            "nom1",
            "adresse3",
            "lieu1",
            "codePostal1",
            "telephone3",
            "url3",
            true
        )

        val list = listOf(activity1, activity2, activity3)

        val sortedList = activitiesRepository.trierParIdentifiant(list)

        val expectedList = listOf(activity3, activity2, activity1)

        Assert.assertEquals(expectedList, sortedList)
    }
}
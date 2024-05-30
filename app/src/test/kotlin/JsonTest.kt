import fr.c1.chatbot.model.Reponse
import fr.c1.chatbot.model.Tree
import fr.c1.chatbot.model.Action
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class JsonTest {

    @Before
    fun setUp() {

    }

    @After
    fun tearDown() {

    }

    @Test
    fun initFuckingTree() {
        val tree = Tree()
        val inputStream = javaClass.classLoader?.getResourceAsStream("flow_chart.json")
        if (inputStream != null) {
            tree.initTree(inputStream)
            Assert.assertEquals(tree.getQuestion(), "Bonjour, que puis-je faire pour vous ?")

            var listReponse : ArrayList<Reponse> = tree.getAnswers()
            Assert.assertEquals(listReponse.size, 3)
            Assert.assertEquals(listReponse[0].id, 1000)
            Assert.assertEquals(listReponse[1].id, 1011)
            Assert.assertEquals(listReponse[2].id, 1012)

            Assert.assertEquals(listReponse[0].text, "Bonjour, je souhaite faire une activité")
            Assert.assertEquals(listReponse[1].text, "Bonjour, je souhaite trouver une association")
            Assert.assertEquals(listReponse[2].text, "Bonjour, j'ai besoin d'aide")

            tree.selectAnswer(1000)
            listReponse = tree.getAnswers()
            Assert.assertEquals(tree.getQuestion(), "Quand est-ce que vous souhaitez la faire ?")
            Assert.assertEquals(listReponse.size, 1)
            Assert.assertEquals(listReponse[0].id, 1001)
            Assert.assertEquals(listReponse[0].text, "Choisir une date")
            Assert.assertEquals(tree.getActionUtilisateur(listReponse[0]), Action.TypeAction.EntrerDate)
        }

    }



}
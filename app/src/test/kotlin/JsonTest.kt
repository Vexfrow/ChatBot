import fr.c1.chatbot.ChatBot
import fr.c1.chatbot.R
import fr.c1.chatbot.model.flowChart.Tree
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.lang.reflect.Array

class JsonTest {

    @Before
    fun setUp() {

    }

    @After
    fun tearDown() {

    }

    @Test
    fun initFuckingTree() {
        val tree = Tree();
        val inputStream = javaClass.classLoader?.getResourceAsStream("flow_chart.json")
        if (inputStream != null) {
            tree.initTree(inputStream)
            Assert.assertEquals(tree.getQuestion(), "Bonjour, que puis-je faire pour vous ?")

            var listReponse : ArrayList<Int> = tree.getAnswersId()
            Assert.assertEquals(listReponse.size, 3)
            Assert.assertEquals(listReponse[0], 1000)
            Assert.assertEquals(listReponse[1], 1011)
            Assert.assertEquals(listReponse[2], 1012)

            Assert.assertEquals(tree.getAnswerText(listReponse[0]), "Bonjour, je souhaite faire une activité")
            Assert.assertEquals(tree.getAnswerText(listReponse[1]), "Bonjour, je souhaite trouver une association")
            Assert.assertEquals(tree.getAnswerText(listReponse[2]), "Bonjour, j'ai besoin d'aide")

            tree.selectAnswer(1000)
            listReponse = tree.getAnswersId()
            Assert.assertEquals(tree.getQuestion(), "Quand est-ce que vous souhaitez la faire ?")
            Assert.assertEquals(listReponse.size, 1)
            Assert.assertEquals(listReponse[0], 1001)
            Assert.assertEquals(tree.getAnswerText(listReponse[0]), "Choisir une date")
            Assert.assertEquals(tree.getActionUtilisateur(listReponse[0]), "date")
        }

    }



}
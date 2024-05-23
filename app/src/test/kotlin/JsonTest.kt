import fr.c1.chatbot.ChatBot
import fr.c1.chatbot.R
import fr.c1.chatbot.model.flowChart.Tree
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class JsonTest {

    private val standardOut: PrintStream = System.out

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
        }else{
            println("baise ta mère")
        }

    }



}
package fr.c1.chatbot.viewModel

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import fr.c1.chatbot.R
import fr.c1.chatbot.composable.History
import fr.c1.chatbot.model.User
import fr.c1.chatbot.model.messageManager.Message
import fr.c1.chatbot.model.messageManager.Tree
import fr.c1.chatbot.model.messageManager.TypeAction
import fr.c1.chatbot.utils.disableNotification
import kotlinx.coroutines.delay
import java.io.InputStream
import kotlin.time.Duration.Companion.seconds

class MessageVM(
    ctx: Context
) : ViewModel() {

    val chatBotTree = Tree()
    private var mapScript: Map<String, InputStream> = mapOf(
        "Rob" to ctx.resources.openRawResource(R.raw.rob),
        "Amy" to ctx.resources.openRawResource(R.raw.amy),
        "Georges" to ctx.resources.openRawResource(R.raw.georges)
    )
    private val messageHistory = mutableStateListOf<Message>()
    val messages get() = messageHistory.toList()

    val optionsAvailable: ArrayList<Message> = ArrayList()

    @SuppressLint("StaticFieldLeak")
    private val context = ctx


    //Initialise le messageManager (mm)
    fun initMessageManager() {
        chatBotTree.initTree(this, mapScript)
    }


    //Rajoute un message à la liste des message
    fun addMessage(message: Message) {
        messageHistory.add(message)
    }

    //S'occupe de gérer les différentes actions
    fun manageActions(action: TypeAction) {
        when (action) {
            TypeAction.None -> {
                //Rompich
            }

            TypeAction.DateInput -> TODO()
            TypeAction.DistanceInput -> TODO()
            TypeAction.CityInput -> TODO()
            TypeAction.ShowResults -> TODO()
            TypeAction.Geolocate -> TODO()
            TypeAction.ChoosePassions -> TODO()
            TypeAction.PhysicalActivity -> TODO()
            TypeAction.CulturalActivity -> TODO()
            TypeAction.DeleteSuggestions -> TODO()
            TypeAction.DeletNotifs -> {
                disableNotification(context)
            }

            TypeAction.Back -> {
                chatBotTree.back()
                var i = messageHistory.size - 1
                while (i > 0 && !messageHistory[i].isUser) {
                    messageHistory.removeAt(i)
                    i--
                }
            }

            TypeAction.Restart -> {
                chatBotTree.restart()
                messageHistory.removeAll(messageHistory.toSet())

            }

            TypeAction.ShowFilters -> {
                messageHistory.add(Message("Voici la liste des filtres utilisés :", false))
            }

            TypeAction.Skip -> TODO()
            TypeAction.DeleteRecalls -> TODO()
        }
    }

    fun selectAnswer(id: Int, user: User) {
        addMessage(Message(chatBotTree.getAnswerText(id), true))
        chatBotTree.selectAnswer(id, user)
        manageActions(chatBotTree.getUserAction(id))
    }
    fun updateQuestion(){
        addMessage(Message(chatBotTree.question, false))
        manageActions(chatBotTree.botAction)
    }


}
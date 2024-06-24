package fr.c1.chatbot.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Location
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import fr.c1.chatbot.ChatBot
import fr.c1.chatbot.R
import fr.c1.chatbot.model.User
import fr.c1.chatbot.model.activity.Type
import fr.c1.chatbot.model.messageManager.Message
import fr.c1.chatbot.model.messageManager.Tree
import fr.c1.chatbot.model.messageManager.TypeAction
import fr.c1.chatbot.utils.LocationHandler
import fr.c1.chatbot.utils.application
import fr.c1.chatbot.utils.disableNotification
import java.io.InputStream

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
    fun manageActions(action: TypeAction, text: String, app: ChatBot, activitiesVM: ActivitiesVM) {
        when (action) {
            TypeAction.None -> {
                addMessage(
                    Message(
                        messageContent = text,
                        isUser = true,
                        isScript = false,
                        showing = true
                    )
                )
            }

            TypeAction.DateInput -> {}
            TypeAction.DistanceInput -> {}
            TypeAction.CityInput -> {}
            TypeAction.ShowResults -> {}
            TypeAction.Geolocate -> {
                app.activitiesRepository.location = LocationHandler.currentLocation ?: Location("")
                addMessage(
                    Message(
                        messageContent = "Je suis ici : ${LocationHandler.currentLocation!!.longitude}, ${LocationHandler.currentLocation?.latitude}",
                        isUser = true,
                        isScript = false,
                        showing = true
                    )
                )
            }

            TypeAction.ChoosePassions -> {
            }

            TypeAction.PhysicalActivity -> activitiesVM.addType(Type.SPORT)
            TypeAction.CulturalActivity -> activitiesVM.addType(Type.CULTURE)
            TypeAction.DeleteSuggestions -> {

            }

            TypeAction.DeletNotifs -> {
                disableNotification(context)
            }

            TypeAction.Back -> {
                chatBotTree.back()
                var i = messageHistory.size - 1
                while (i > 0 && !messageHistory[i].isScript) {
                    messageHistory.removeAt(i)
                    i--
                }
                activitiesVM.undo()
            }

            TypeAction.Restart -> {
                messageHistory.removeAll(messageHistory.toSet())
                chatBotTree.restart()
                activitiesVM.reset()
            }

            TypeAction.ShowFilters -> {
                messageHistory.add(
                    Message(
                        messageContent = "Voici la liste des filtres utilisés : \n" +
                                "Villes : ${app.currentUser.cities}\n" +
                                "Types d'activités : ${app.currentUser.types}\n" +
                                "Distance : ${activitiesVM.distance}\n" +
                                "Dates: ${activitiesVM.date}\n",
                        isUser = false,
                        isScript = false,
                        showing = true
                    )
                )
            }

            TypeAction.Skip -> TODO()
        }
    }

    fun selectAnswer(id: Int, text: String?, user: User, app: ChatBot, activitiesVM: ActivitiesVM) {

        chatBotTree.selectAnswer(id, user)
        if (text != null)
            manageActions(chatBotTree.getUserAction(id), text, app, activitiesVM)
        else
            manageActions(
                chatBotTree.getUserAction(id),
                chatBotTree.getAnswerText(id),
                app,
                activitiesVM
            )
    }

    fun updateQuestion(app: ChatBot, activitiesVM: ActivitiesVM) {
        addMessage(Message(messageContent = chatBotTree.question, isUser = false, isScript = true))
        manageActions(chatBotTree.botAction, chatBotTree.question, app, activitiesVM)
    }


}
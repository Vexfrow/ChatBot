package fr.c1.chatbot.viewModel

import android.annotation.SuppressLint
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

    private val optionsAvailable = mutableStateListOf<String>()
    val answers get() = optionsAvailable.toList()

    @SuppressLint("StaticFieldLeak")
    private val context = ctx


    //Initialise le messageManager (mm)
    fun initMessageManager() {
        chatBotTree.initTree(this, mapScript)
        chatBotTree.answersId.map { optionsAvailable.add(chatBotTree.getAnswerText(it))}
    }

    //Rajoute un message à la liste des message
    fun addMessage(message: Message) {
        messageHistory.add(message)
    }

    //S'occupe de gérer les différentes actions
    private fun manageActions(action: TypeAction, app: ChatBot, activitiesVM: ActivitiesVM) {
        when (action) {
            TypeAction.None -> {

            }

            TypeAction.DateInput -> {}
            TypeAction.DistanceInput -> {}
            TypeAction.CityInput -> {}
            TypeAction.ShowResults -> {}
            TypeAction.Geolocate -> {
                messageHistory.removeLast()
                app.activitiesRepository.location = LocationHandler.currentLocation ?: Location("")
                addMessage(
                    Message(
                        messageContent = "Je suis situé ici :\n" +
                                "\t ${LocationHandler.currentLocation!!.longitude}\n" +
                                "\t ${LocationHandler.currentLocation?.latitude}",
                        isUser = true,
                        isScript = false,
                        showing = true
                    )
                )
            }

            TypeAction.ChoosePassions -> {
            }

            TypeAction.PhysicalActivity -> {
                messageHistory.removeLast()
                messageHistory.add(
                    Message(
                        "Je souhaite faire une activité physique",
                        isUser = true,
                        isScript = false,
                        showing = true
                    )
                )
                activitiesVM.addType(Type.SPORT)
            }

            TypeAction.CulturalActivity -> {
                messageHistory.removeLast()
                messageHistory.add(
                    Message(
                        "Je souhaite faire une activité culturelle",
                        isUser = true,
                        isScript = false,
                        showing = true
                    )
                )
                activitiesVM.addType(Type.CULTURE)
            }

            TypeAction.DeleteNotifs -> {
                disableNotification(context)
            }

            TypeAction.Back -> {
                chatBotTree.back()
                var i = messageHistory.size - 1
                while (i > 0 && !messageHistory[i].isScript) {
                    messageHistory.removeAt(i)
                    i--
                }
                if (i > 0)
                    messageHistory.removeLast()

                activitiesVM.undo()
            }

            TypeAction.Restart -> {
                messageHistory.removeAll(messageHistory.toSet())
                chatBotTree.restart()
                activitiesVM.reset()
            }

            TypeAction.ShowFilters -> {
                messageHistory.removeLast()
                messageHistory.add(
                    Message(
                        "Je souhaite afficher les filtres utilisés pour ma recherche",
                        isUser = true,
                        isScript = false,
                        showing = true
                    )
                )


                messageHistory.add(
                    Message(
                        messageContent = "Voici la liste des filtres utilisés : \n" +
                                if (app.currentUser.cities.isNotEmpty()) "Villes : ${app.currentUser.cities}\n" else {
                                    "Villes : Aucunes sélectionnées\n\n"
                                } +
                                if (app.currentUser.types.isNotEmpty()) "Types d'activités : ${app.currentUser.types}\n" else {
                                    "Types d'activités : Aucuns sélectionnés\n\n"
                                } +
                                "Distance : Corenthin ne veut pas me donner la distance\n\n" +
                                "Dates: Corenthin est un petit con qui ne veut pas faire de date avec moi ><\n\n",
                        isUser = false,
                        isScript = false,
                        showing = true
                    )
                )
            }


            //No differences between all of them
            TypeAction.BigSportif -> {
                messageHistory.removeLast()
                messageHistory.add(
                    Message(
                        "Je fait du sport régulièrement",
                        isUser = true,
                        isScript = false,
                        showing = true
                    )
                )
            }

            TypeAction.LittleSportif -> {
                messageHistory.removeLast()
                messageHistory.add(
                    Message(
                        "Je fait du sport de temps en temps",
                        isUser = true,
                        isScript = false,
                        showing = true
                    )
                )
            }

            TypeAction.InexistantSportif -> {
                messageHistory.removeLast()
                messageHistory.add(
                    Message(
                        "Je fait très peu d'activités physiques",
                        isUser = true,
                        isScript = false,
                        showing = true
                    )
                )
            }
        }
    }

    fun selectAnswer(id: Int, text: String?, user: User, app: ChatBot, activitiesVM: ActivitiesVM) {
        optionsAvailable.removeAll(optionsAvailable.toSet())
        chatBotTree.selectAnswer(id, user)
        if (text != null)
            addMessage(Message(text, isUser = true, isScript = false, showing = true))
        else
            addMessage(
                Message(
                    chatBotTree.getAnswerText(id),
                    isUser = true,
                    isScript = false,
                    showing = true
                )
            )

        manageActions(
            chatBotTree.getUserAction(id),
            app,
            activitiesVM
        )
    }

    fun updateQuestion(app: ChatBot, activitiesVM: ActivitiesVM) {
        addMessage(
            Message(
                messageContent = chatBotTree.question,
                isUser = false,
                isScript = true,
                showing = true
            )
        )
        manageActions(chatBotTree.botAction, app, activitiesVM)

        chatBotTree.answersId.map { optionsAvailable.add(chatBotTree.getAnswerText(it))}
    }
}
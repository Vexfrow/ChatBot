package fr.c1.chatbot.viewModel

import fr.c1.chatbot.ChatBot
import fr.c1.chatbot.R
import fr.c1.chatbot.model.activity.Type
import fr.c1.chatbot.model.messageManager.Message
import fr.c1.chatbot.model.messageManager.Tree
import fr.c1.chatbot.model.messageManager.TypeAction
import fr.c1.chatbot.utils.LocationHandler
import fr.c1.chatbot.utils.TTS
import fr.c1.chatbot.utils.disableNotification
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import fr.c1.chatbot.model.Settings
import fr.c1.chatbot.utils.toDate
import java.io.InputStream
import java.time.LocalDate

class MessageVM(
    ctx: Context,
    private val tts: TTS
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
        chatBotTree.answersId.map { optionsAvailable.add(chatBotTree.getAnswerText(it)) }
        addMessage(Message(chatBotTree.question, isUser = false, isScript = true))
    }

    //Rajoute un message à la liste des message
    private fun addMessage(message: Message) {
        messageHistory.add(message)
        if (!message.isUser && Settings.tts)
            tts.speak(message.messageContent)
    }

    //S'occupe de gérer les différentes actions
    private fun manageActions(action: TypeAction, app: ChatBot, activitiesVM: ActivitiesVM) {
        when (action) {
            TypeAction.None -> {}
            TypeAction.DateInput -> {}
            TypeAction.DistanceInput -> {}
            TypeAction.CityInput -> {}
            TypeAction.ShowResults -> {}
            TypeAction.Geolocation -> {
                messageHistory.removeLast()
                app.activitiesRepository.location = LocationHandler.currentLocation ?: Location("")
                addMessage(
                    Message(
                        messageContent = "Je suis situé ici :\n" +
                                "\t ${LocationHandler.currentLocation!!.longitude}\n" +
                                "\t ${LocationHandler.currentLocation?.latitude}",
                        isUser = true,
                        isScript = false
                    )
                )
            }

            TypeAction.ChoosePassions -> {}

            TypeAction.PhysicalActivity -> {
                messageHistory.removeLast()
                addMessage(
                    Message(
                        "Je souhaite faire une activité physique",
                        isUser = true,
                        isScript = false
                    )
                )
                activitiesVM.addType(Type.SPORT)
            }

            TypeAction.CulturalActivity -> {
                messageHistory.removeLast()
                addMessage(
                    Message(
                        "Je souhaite faire une activité culturelle",
                        isUser = true,
                        isScript = false
                    )
                )
                activitiesVM.addType(Type.CULTURE)
            }

            TypeAction.DeleteNotifs -> {
                disableNotification(context)
            }

            TypeAction.Back -> {
                messageHistory.removeLast()
                chatBotTree.back()
                if (messageHistory.size > 0)
                    messageHistory.removeLast()

                while (messageHistory.isNotEmpty() && (messageHistory.last().isUser || !messageHistory.last().isScript)) {
                    messageHistory.removeLast()
                }

                if (messageHistory.size > 0)
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
                addMessage(
                    Message(
                        "Je souhaite afficher les filtres utilisés pour ma recherche",
                        isUser = true,
                        isScript = false
                    )
                )
                addMessage(
                    Message(
                        messageContent = "Voici la liste des filtres utilisés",
                        isUser = false,
                        isScript = false
                    )
                )
                addMessage(
                    Message(
                        messageContent = if (activitiesVM.user.cities.isNotEmpty()) "Villes : ${activitiesVM.user.cities}" else "Villes : Aucunes villes sélectionnées",
                        isUser = false,
                        isScript = false
                    )
                )
                addMessage(
                    Message(
                        messageContent = if (activitiesVM.user.types.isNotEmpty()) "Types d'activités : ${activitiesVM.user.types}" else "Types d'activités : Aucuns type d'activités sélectionnés",
                        isUser = false,
                        isScript = false
                    )
                )
                addMessage(
                    Message(
                        messageContent = if (activitiesVM.distance > -1) "Distance : ${activitiesVM.distance}" else "Distance : Aucune distance sélectionnée",
                        isUser = false,
                        isScript = false
                    )
                )
                addMessage(
                    Message(
                        messageContent = if (activitiesVM.date.isNotEmpty()) "Date : ${activitiesVM.date}" else "Date : Aucune date sélectionnée",
                        isUser = false,
                        isScript = false
                    )
                )
            }


            //No differences between all of them
            TypeAction.BigSportif -> {
                messageHistory.removeLast()
                addMessage(
                    Message(
                        "Je fait du sport régulièrement",
                        isUser = true,
                        isScript = false
                    )
                )
            }

            TypeAction.LittleSportif -> {
                messageHistory.removeLast()
                addMessage(
                    Message(
                        "Je fait du sport de temps en temps",
                        isUser = true,
                        isScript = false
                    )
                )
            }

            TypeAction.InexistantSportif -> {
                messageHistory.removeLast()
                addMessage(
                    Message(
                        "Je fait très peu d'activités physiques",
                        isUser = true,
                        isScript = false
                    )
                )
            }

            TypeAction.Meeting -> {
                messageHistory.removeLast()
                addMessage(
                    Message(
                        "Je souhaite rencontrer d'autres personnes",
                        isUser = true,
                        isScript = false
                    )
                )
            }

            TypeAction.NoMeeting -> {
                messageHistory.removeLast()
                addMessage(
                    Message(
                        "Je ne souhaite pas rencontrer d'autres personnes",
                        isUser = true,
                        isScript = false
                    )
                )
            }

            TypeAction.PerhapsMeeting -> {
                messageHistory.removeLast()
                addMessage(
                    Message(
                        "Cela m'importe peu",
                        isUser = true,
                        isScript = false
                    )
                )
            }

            TypeAction.Today -> {
                messageHistory.removeLast()
                addMessage(Message("Je souhaite faire une activité aujourd'hui", isUser = true, isScript = false))
                activitiesVM.date = System.currentTimeMillis().toDate()

            }
            TypeAction.Tomorrow -> {
                messageHistory.removeLast()
                addMessage(Message("Je souhaite faire une activité demain", isUser = true, isScript = false))
                activitiesVM.date = LocalDate.now().plusDays(1).toString()
            }
        }
    }

    fun selectAnswer(id: Int, text: String?, app: ChatBot, activitiesVM: ActivitiesVM) {
        optionsAvailable.removeAll(optionsAvailable.toSet())
        chatBotTree.selectAnswer(id)
        if (text != null)
            addMessage(Message(text, isUser = true, isScript = false))
        else
            addMessage(
                Message(
                    chatBotTree.getAnswerText(id),
                    isUser = true,
                    isScript = false
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
                isScript = true
            )
        )
        manageActions(chatBotTree.botAction, app, activitiesVM)

        chatBotTree.answersId.map { optionsAvailable.add(chatBotTree.getAnswerText(it)) }
    }
}
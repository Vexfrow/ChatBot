package fr.c1.chatbot.composable

import fr.c1.chatbot.composable.utils.BotLoading
import fr.c1.chatbot.composable.utils.MyText
import fr.c1.chatbot.model.ActivitiesRepository
import fr.c1.chatbot.model.messageManager.TypeAction
import fr.c1.chatbot.utils.Resource
import fr.c1.chatbot.utils.UnitLaunchedEffect
import fr.c1.chatbot.utils.application
import fr.c1.chatbot.utils.rememberMutableStateOf
import fr.c1.chatbot.viewModel.ActivitiesVM
import fr.c1.chatbot.viewModel.MessageVM
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import android.util.Log
import kotlin.time.Duration.Companion.seconds

private const val TAG = "ChatBotComp"

object ChatBotComp {
    @Composable
    fun Chat(
        activitiesVM: ActivitiesVM,
        messageVM: MessageVM,
        modifier: Modifier = Modifier,
        onResult: () -> Unit
    ) {
        val app = application
        val crtScope = rememberCoroutineScope()
        val lazyListState = rememberLazyListState()

        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                state = lazyListState
            ) {
                items(messageVM.messages) { message ->
                    val scale by animateFloatAsState(
                        targetValue = if (message.showed) 1f else 0f,
                        animationSpec = tween(500)
                    )

                    if (!message.showed)
                        UnitLaunchedEffect {
                            message.showed = true
                        }

                    val mod = Modifier.graphicsLayer(
                        scaleX = scale, scaleY = scale
                    )

                    if (!message.isUser) MessageComponent(
                        modifier = if (message == messageVM.messages.last()) mod else Modifier,
                        text = message.messageContent,
                        isUser = false
                    )
                    else Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(if (message == messageVM.messages.last()) mod else Modifier)
                    ) {
                        MessageComponent(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            text = message.messageContent,
                            isUser = true
                        )
                    }
                }
            }

            var sbState by rememberMutableStateOf(SearchBarState())

            fun enableSearchBar(
                text: String,
                act: TypeAction,
                id: Int,
                list: Collection<String>? = null
            ) {
                sbState = SearchBarState(true, text, act, id, list)
            }

            fun reset() {
                sbState = SearchBarState()
            }

            fun addAnswer(id: Int, text: String? = null) {
                reset()
                messageVM.selectAnswer(id, text, app, activitiesVM)

                crtScope.launch {
                    lazyListState.animateScrollToItem(messageVM.messages.size)
                    delay(1.seconds)
                    messageVM.updateQuestion(app, activitiesVM)
                    lazyListState.animateScrollToItem(messageVM.messages.size)

                    if (messageVM.chatBotTree.botAction == TypeAction.ShowResults) {
                        delay(5.seconds)
                        onResult()
                    }
                }
            }

            Proposals(
                proposals = messageVM.answers,
            ) {
                Log.i(TAG, "Choose '$it'")
                val i = messageVM.chatBotTree.answersId.first { i ->
                    messageVM.chatBotTree.getAnswerText(i) == it
                }
                when (val act = messageVM.chatBotTree.getUserAction(i)) {
                    TypeAction.DateInput -> {
                        enableSearchBar("Sélectionnez une date", act, i)
                        return@Proposals
                    }

                    TypeAction.DistanceInput -> {
                        enableSearchBar("Saisissez une distance", act, i)
                        return@Proposals
                    }

                    TypeAction.CityInput -> {
                        enableSearchBar(
                            "Saisissez une ville", act, i, app.activitiesRepository.cities
                        )
                        return@Proposals
                    }

                    TypeAction.ShowResults -> {
                        Log.i(TAG, "MyColumn: Affichage des résultats")
                    }

                    TypeAction.ChoosePassions -> {
                        val passions = ActivitiesRepository.passionList
                        enableSearchBar("Choisissez une passion", act, i, passions)
                        return@Proposals
                    }


                    else -> Log.i(TAG, "Chat: Action not implemented: $act")
                }

                addAnswer(i)
            }

            fun search(value: String) {
                when (sbState.action) {
                    //If the action is to put a date
                    TypeAction.DateInput -> {
                        addAnswer(sbState.answerId, "Je veux faire une activité le $value")
                        activitiesVM.date = value
                    }

                    //If the action is to put a distance
                    TypeAction.DistanceInput -> {
                        addAnswer(
                            sbState.answerId,
                            "Je peux me déplacer sur une distance de $value kilomètres"
                        )
                        activitiesVM.distance = value.toInt()
                    }


                    //If the action is to put the name of a city
                    TypeAction.CityInput -> {
                        activitiesVM.city = value
                        val city =
                            if ("AEIOUaeiou".indexOf(value.first()) != -1) "d'$value" else "de $value"
                        addAnswer(
                            sbState.answerId,
                            "Je souhaite faire mon activité dans les alentours de la ville $city"
                        )
                    }

                    else -> Log.i(TAG, "search: Action not implemented: ${sbState.action}")
                }
                Log.i(TAG, "Searched $value")
            }

            MySearchBar(
                placeholder = sbState.text,
                enabled = sbState.enabled,
                action = sbState.action,
                proposals = sbState.list,
                onSearch = ::search
            )
        }
    }

    @Composable
    fun Result(activitiesVM: ActivitiesVM) = when (val result = activitiesVM.result) {
        is Resource.None -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            MyText(
                text = "Veuillez commencer à échanger avec le robot pour obtenir des résultats",
                textAlign = TextAlign.Center
            )
        }

        is Resource.Loading -> BotLoading()

        is Resource.Success -> ActivitiesComp(list = result.data!!)
        is Resource.Failed -> ToDo(name = "Failed: ${result.error}")

        else -> throw NotImplementedError()
    }
}

private data class SearchBarState(
    val enabled: Boolean,
    val text: String,
    val action: TypeAction,
    val answerId: Int,
    val list: Collection<String>?
) {
    constructor() : this(
        false,
        "Choisissez une option ci-dessus",
        TypeAction.None,
        0,
        null
    )
}
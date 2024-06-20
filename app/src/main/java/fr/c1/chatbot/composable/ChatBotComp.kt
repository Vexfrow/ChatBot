package fr.c1.chatbot.composable

import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import fr.c1.chatbot.composable.utils.MyText
import fr.c1.chatbot.model.ActivitiesRepository
import fr.c1.chatbot.model.Settings
import fr.c1.chatbot.model.TypeAction
import fr.c1.chatbot.model.activity.Type.CULTURE
import fr.c1.chatbot.model.activity.Type.SPORT
import fr.c1.chatbot.utils.LocationHandler
import fr.c1.chatbot.utils.Resource
import fr.c1.chatbot.utils.application
import fr.c1.chatbot.utils.rememberMutableStateOf
import fr.c1.chatbot.viewModel.ActivitiesVM
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import android.location.Location
import android.util.Log
import kotlin.time.Duration.Companion.seconds

private const val TAG = "ChatBotComp"

object ChatBotComp {
    @Composable
    fun Chat(
        messages: SnapshotStateList<String>,
        animated: SnapshotStateList<Boolean>,
        activitiesVM: ActivitiesVM,
        modifier: Modifier = Modifier,
        onResult: () -> Unit
    ) {
        val app = application
        val tree = app.chatbotTree
        val crtScope = rememberCoroutineScope()
        val lazyListState = rememberLazyListState()
        val user = app.currentUser

        val tts = application.tts

        LaunchedEffect(key1 = messages.size) {
            if (Settings.tts && messages.lastIndex % 2 == 0)
                tts.speak(messages.last())
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Settings.backgroundColor)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Settings.backgroundColor),
                state = lazyListState
            ) {
                itemsIndexed(messages) { i, message ->
                    val scale: Animatable<Float, AnimationVector1D> =
                        remember { Animatable(0f) }

                    LaunchedEffect(key1 = Unit) {
                        scale.animateTo(
                            1f,
                            animationSpec = tween(durationMillis = 500)
                        ) {
                            if (value == 1f)
                                animated.add(true)
                        }
                    }

                    val isBot = i % 2 == 0
                    val mod = Modifier.graphicsLayer(
                        scaleX = scale.value,
                        scaleY = scale.value
                    )

                    if (isBot)
                        Message(
                            modifier = if (i == messages.lastIndex) mod else Modifier,
                            text = message
                        )
                    else
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .then(if (i == messages.lastIndex) mod else Modifier)
                        ) {
                            Message(
                                modifier = Modifier.align(Alignment.CenterEnd),
                                text = message,
                                isUser = true
                            )
                        }
                }
            }

            var answers by rememberMutableStateOf(value = tree.answersId
                .map { tree.getAnswerText(it) })

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

            fun addAnswer(id: Int, answer: String? = null) {
                reset()
                messages.add(answer ?: tree.getAnswerText(id))
                tree.selectAnswer(id, user)

                crtScope.launch {
                    lazyListState.animateScrollToItem(messages.size)
                    delay(1.seconds)
                    messages += tree.question
                    answers = tree.answersId.map { i -> tree.getAnswerText(i) }
                    lazyListState.animateScrollToItem(messages.size)

                    if (tree.botAction == TypeAction.ShowResults) {
                        delay(5.seconds)
                        onResult()
                    }
                }
            }

            Proposals(
                modifier = Modifier.background(Settings.backgroundColor),
                proposals = answers,
            ) {
                answers = emptyList()
                Log.i(TAG, "Choose '$it'")
                val i = tree.answersId.first { i -> tree.getAnswerText(i) == it }
                when (val act = tree.getUserAction(i)) {
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
                            "Saisissez une ville",
                            act,
                            i,
                            app.activitiesRepository.cities
                        )
                        return@Proposals
                    }

                    TypeAction.ShowResults -> {
                        Log.i(TAG, "MyColumn: Affichage des résultats")
                    }

                    TypeAction.Geolocate -> {
                        app.activitiesRepository.location =
                            LocationHandler.currentLocation ?: Location("")
                        addAnswer(
                            i,
                            "Je suis ici : ${LocationHandler.currentLocation!!.longitude}, ${LocationHandler.currentLocation?.latitude}"
                        )
                        return@Proposals
                    }

//                TypeAction.ChoisirPassions -> TODO()

                    TypeAction.PhysicalActivity -> activitiesVM.addType(SPORT)
                    TypeAction.CulturalActivity -> activitiesVM.addType(CULTURE)

                    TypeAction.ChoosePassions -> {
                        val passions = ActivitiesRepository.passionList
                        enableSearchBar("Choisissez une passion", act, i, passions)
                        return@Proposals
                    }

                    else -> {}
                }

                addAnswer(i)
            }

            fun search(value: String) {
                when (sbState.action) {
                    TypeAction.DateInput -> {
                        addAnswer(sbState.answerId, "Je veux y aller le $value")
                        activitiesVM.date = value
                    }

                    TypeAction.DistanceInput -> {
                        addAnswer(sbState.answerId, "Je veux une distance de $value km")
                        activitiesVM.distance = value.toInt()
                    }

                    TypeAction.CityInput -> {
                        activitiesVM.city = value
                        val text =
                            if ("AEIOUaeiou".indexOf(value.first()) != -1) "d'$value" else "de $value"
                        addAnswer(
                            sbState.answerId,
                            "Je souhaite faire mon activité dans les alentours de la ville $text"
                        )
                    }

                    else -> {}
                }
                Log.i(TAG, "Searched $value")
            }

            MySearchBar(
                modifier = Modifier.background(Settings.backgroundColor),
                placeholder = sbState.text,
                enabled = sbState.enabled,
                action = sbState.action,
                proposals = sbState.list,
                onSearch = ::search
            )
        }
    }

    const val rotationRange = 45f
    const val rotationDuration = 500

    @Composable
    fun Result(activitiesVM: ActivitiesVM) {
        when (val result = activitiesVM.result) {
            is Resource.None -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                MyText(
                    text = "Veuillez commencer à échanger avec le robot pour obtenir des résultats",
                    textAlign = TextAlign.Center
                )
            }

            is Resource.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val ir = rememberInfiniteTransition()
                val rot by ir.animateFloat(
                    initialValue = -rotationRange,
                    targetValue = rotationRange,
                    animationSpec = infiniteRepeatable(
                        animation = keyframes {
                            durationMillis = rotationDuration * 4
                            0f at 0 using LinearEasing
                            rotationRange at rotationDuration using LinearEasing
                            0f at rotationDuration * 2 using LinearEasing
                            (-rotationRange) at rotationDuration * 3 using LinearEasing
                            0f at rotationDuration * 4 using LinearEasing
                        },
                        repeatMode = RepeatMode.Restart
                    ), label = ""
                )

                CircularProgressIndicator(
                    modifier = Modifier.size(96.dp)
                )

                if (Settings.botImage == null)
                    Icon(
                        modifier = Modifier
                            .size(48.dp)
                            .graphicsLayer(rotationZ = rot),
                        imageVector = Settings.botIcon,
                        contentDescription = "Robot loading"
                    )
                else
                    Image(
                        modifier = Modifier
                            .size(48.dp)
                            .graphicsLayer(rotationZ = rot),
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(Settings.botImage)
                                .build()
                        ),
                        contentDescription = "Robot loading"
                    )
            }

            is Resource.Success -> ActivitiesComp(list = result.data!!)
            is Resource.Failed -> ToDo(name = "Failed: ${result.error}")

            else -> throw NotImplementedError()
        }
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
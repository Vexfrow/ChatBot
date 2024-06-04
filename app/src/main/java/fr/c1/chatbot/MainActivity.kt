package fr.c1.chatbot

import fr.c1.chatbot.composable.Activities
import fr.c1.chatbot.composable.Message
import fr.c1.chatbot.composable.MySearchBar
import fr.c1.chatbot.composable.MySettings
import fr.c1.chatbot.composable.ProposalList
import fr.c1.chatbot.composable.Tab
import fr.c1.chatbot.composable.TopBar
import fr.c1.chatbot.model.ActivitiesRepository
import fr.c1.chatbot.model.Event
import fr.c1.chatbot.model.Settings
import fr.c1.chatbot.model.TypeAction
import fr.c1.chatbot.model.toDate
import fr.c1.chatbot.ui.theme.ChatBotTheme
import fr.c1.chatbot.ui.theme.colorSchemeExtension
import fr.c1.chatbot.utils.Calendar
import fr.c1.chatbot.utils.application
import fr.c1.chatbot.utils.rememberMutableStateListOf
import fr.c1.chatbot.utils.rememberMutableStateOf
import fr.c1.chatbot.utils.scheduleEventReminders
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.work.WorkManager
import android.Manifest
import android.os.Bundle
import android.util.Log
import kotlin.time.Duration.Companion.seconds

private const val TAG = "MainActivity"

private var initNotif = false

class MainActivity : ComponentActivity() {
    private lateinit var workManager: WorkManager
    private lateinit var app: ChatBot
    private lateinit var activitiesRepository: ActivitiesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        workManager = WorkManager.getInstance(this)
        app = application as ChatBot
        activitiesRepository = app.activitiesRepository
        activitiesRepository.initAll(application)
        enableEdgeToEdge()
        setContent {
            ChatBotTheme {
                PermissionsContent()
                PermissionNotification()

                val ctx = LocalContext.current

                var tab by rememberMutableStateOf(value = Tab.ChatBotChat)

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopBar(tab.value) {
                            val tmp = Tab.valueOf(it)

                            if (tab == Tab.Settings && tmp != Tab.Settings)
                                Settings.save(ctx)

                            tab = tmp
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        MyColumn(modifier = Modifier, enabled = tab == Tab.ChatBotChat)
                        when (tab) {
                            Tab.Settings -> MySettings()
                            Tab.ChatBotResults -> Activities(
                                list = app.activitiesRepository.getResultats()
                            )
                            else -> {}
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun PermissionsContent() {
        val context = LocalContext.current

        var hasReadPermission by remember { mutableStateOf(false) }
        var hasWritePermission by remember { mutableStateOf(false) }
        var events by remember { mutableStateOf<List<Event>>(emptyList()) }

        val requestPermissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            hasReadPermission = permissions[Manifest.permission.READ_CALENDAR] == true
            hasWritePermission = permissions[Manifest.permission.WRITE_CALENDAR] == true
        }

        LaunchedEffect(Unit) {
            if (!Calendar.PermissionsRequest.hasReadCalendarPermission(context) || !Calendar.PermissionsRequest.hasWriteCalendarPermission(
                    context
                )
            ) {
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.READ_CALENDAR,
                        Manifest.permission.WRITE_CALENDAR
                    )
                )
            } else {
                hasReadPermission = true
                hasWritePermission = true
            }
        }

        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            if (hasReadPermission && hasWritePermission) {
                events = Calendar.fetchCalendarEvents(context)
                addNotifPush(events)
                //EventList(events, Modifier.padding(innerPadding))
            } else {
                Text("Requesting permissions...", modifier = Modifier.padding(innerPadding))
            }
        }
    }

    private fun addNotifPush(events: List<Event>) {
        if (initNotif) return
        workManager.cancelAllWork()
        Log.d(TAG, "onCreate: cancelAllWork()")
        for (event in events) {
            if (event.dtStart >= System.currentTimeMillis() + (1000 * 60 * 60)) { // Si l'event est dans le futur (dans 1h minimum)
                Log.i(TAG, "onCreate: ${event.title}")
                scheduleEventReminders(this, event.title, event.dtStart)
            }
        }
        // Afficher tous les notifs programmées (log)
        workManager.getWorkInfosByTagLiveData("EventReminderWorker")
            .observe(this) { workInfos ->
                for (workInfo in workInfos) {
                    if (workInfo.state.isFinished) {
                        Log.d(
                            TAG,
                            "onCreate: delete : ${workInfo.id}, ${workInfo.state}, ${workInfo.nextScheduleTimeMillis.toDate()}"
                        )
                        workManager.cancelWorkById(workInfo.id)
                    } else {
                        Log.i(
                            TAG,
                            "onCreate: Notif ajoutée : ${workInfo.id}, ${workInfo.state}, exécution programmée le ${workInfo.nextScheduleTimeMillis.toDate()}"
                        )
                    }
                }
            }
        initNotif = true
    }
}

@Composable
fun PermissionNotification() {
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.RECEIVE_BOOT_COMPLETED] == true &&
            permissions[Manifest.permission.POST_NOTIFICATIONS] == true
        ) {
            Log.i(TAG, "Notifications Permissions granted")
        } else {
            Log.i(TAG, "Notifications Permissions denied")
        }
    }

    LaunchedEffect(Unit) {
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.RECEIVE_BOOT_COMPLETED,
                Manifest.permission.POST_NOTIFICATIONS
            )
        )
    }
}

@Composable
fun MyColumn(modifier: Modifier = Modifier, enabled: Boolean) {
    val app = application
    val tree = app.chatbotTree
    val messages = rememberMutableStateListOf(tree.getQuestion())
    val crtScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val animated = rememberMutableStateListOf<Boolean>()

    val activitiesRepository = application.activitiesRepository

    val tts = application.tts

    if (!enabled)
        return

    LaunchedEffect(key1 = messages.size) {
        if (Settings.tts && messages.lastIndex % 2 == 0)
            tts.speak(messages.last())
    }

    Column(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
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
                        text = message,
                        color = MaterialTheme.colorSchemeExtension.bot,
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
                            color = MaterialTheme.colorSchemeExtension.user,
                            isUser = true
                        )
                    }
            }
        }

        var answers by rememberMutableStateOf(value = tree.getAnswersId()
            .map { tree.getAnswerText(it) })

        data class SearchBarState(
            val enabled: Boolean,
            val text: String,
            val action: TypeAction,
            val answerId: Int,
            val list: List<String>?
        ) {
            constructor() : this(false, "Choisissez une option ci-dessus", TypeAction.None, 0, null)
        }

        var sbState by rememberMutableStateOf(SearchBarState())

        fun enableSearchBar(text: String, act: TypeAction, id: Int, list: List<String>? = null) {
            sbState = SearchBarState(true, text, act, id, list)
        }

        fun reset() {
            sbState = SearchBarState()
        }

        fun addAnswer(id: Int, answer: String? = null) {
            reset()
            messages += answer ?: tree.getAnswerText(id)
            tree.selectAnswer(id, activitiesRepository)

            crtScope.launch {
                lazyListState.animateScrollToItem(messages.size)
                delay(1.seconds)
                messages += tree.getQuestion()
                answers = tree.getAnswersId().map { i -> tree.getAnswerText(i) }
                lazyListState.animateScrollToItem(messages.size)

                if (answers.isEmpty())
                    Log.i(TAG, "MyColumn: Result avaibles !")
            }
        }

        ProposalList(proposals = answers) {
            answers = emptyList()
            Log.i(TAG, "Choose '$it'")
            val i = tree.getAnswersId().first { i -> tree.getAnswerText(i) == it }
            when (val act = tree.getActionUtilisateur(i)) {
                TypeAction.EntrerDate -> {
                    enableSearchBar("Sélectionnez une date", act, i)
                    return@ProposalList
                }

                TypeAction.EntrerDistance -> {
                    enableSearchBar("Saisissez une distance", act, i)
                    return@ProposalList
                }

                TypeAction.EntrerVille -> {
                    enableSearchBar(
                        "Saisissez une ville",
                        act,
                        i,
                        listOf("Grenoble", "Annecy", "Valence")
                    )
                    return@ProposalList
                }

                TypeAction.AfficherResultat -> {
                    Log.i(
                        TAG,
                        "MyColumn: Affichage des résultats: ${app.activitiesRepository.getResultats()}"
                    )
                }

                TypeAction.ChoisirPassions -> TODO()
                else -> {}
            }

            addAnswer(i)
        }

        MySearchBar(
            placeholder = sbState.text,
            enabled = sbState.enabled,
            action = sbState.action,
            proposals = sbState.list
        ) {
            when (sbState.action) {
                TypeAction.EntrerDate -> {
                    app.activitiesRepository.setDate(it)
                    addAnswer(sbState.answerId, "Je veux y aller le $it")
                }

                TypeAction.EntrerDistance -> {
                    app.activitiesRepository.setDistance(it.toInt())
                    addAnswer(sbState.answerId, "Je veux une distance de $it km")
                }

                TypeAction.EntrerVille -> {
                    app.activitiesRepository.addVille(it)
                    addAnswer(sbState.answerId, "Je veux aller dans la ville de $it")
                }

                else -> {}
            }
            Log.i(TAG, "Searched $it")
        }
    }
}
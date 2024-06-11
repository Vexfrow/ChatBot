package fr.c1.chatbot

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
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
import org.osmdroid.config.Configuration
import org.osmdroid.views.MapView
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
import androidx.core.app.ActivityCompat
import androidx.preference.PreferenceManager
import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Surface
import android.widget.Button
import android.widget.TextView
import androidx.work.WorkManager
import fr.c1.chatbot.model.activity.Type.*
import fr.c1.chatbot.utils.*
import java.util.Date
import androidx.compose.ui.viewinterop.AndroidView
import fr.c1.chatbot.utils.LocationHandler
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import kotlin.time.Duration.Companion.seconds

private const val TAG = "MainActivity"

private var initNotif = false

private var currentLocation: Location? = null
private var locationHandler: LocationHandler = LocationHandler

class MainActivity : ComponentActivity() {
    private lateinit var workManager: WorkManager
    private lateinit var app: ChatBot
    private lateinit var activitiesRepository: ActivitiesRepository
    private var requestingLocationUpdates: Boolean = false
    private lateinit var myOpenMapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        workManager = WorkManager.getInstance(this)
        app = application as ChatBot
        activitiesRepository = app.activitiesRepository
        activitiesRepository.initAll(application)
        enableEdgeToEdge()
        //handle permissions first, before map is created. not depicted here

        //load/initialize the osmdroid configuration, this can be done
        // This won't work unless you have imported this: org.osmdroid.config.Configuration.*
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        locationHandler.initLocation(this)

        //locationHandler.startLocationUpdates(this)

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
                        MyColumn(modifier = Modifier, enabled = tab == Tab.ChatBotChat) {
                            tab = Tab.ChatBotResults
                        }

                        when (tab) {
                            Tab.Settings -> MySettings()
                            Tab.ChatBotResults -> Activities(
                                list = app.activitiesRepository.getResultats(app)
                            )

                            else -> {}
                        }
                    }
                }
                // A surface container using the 'background' color from the theme
            }

        }
    }

    @Composable
    fun PermissionsContent() {
        val context = LocalContext.current

        var hasReadPermission by remember { mutableStateOf(false) }
        var hasWritePermission by remember { mutableStateOf(false) }
        var events by remember { mutableStateOf<List<Event>>(emptyList()) }

        var hasFineLocation by remember { mutableStateOf(false) }
        var hasCoarseLocation by remember { mutableStateOf(false) }

        var permissionsArray: Array<String> = arrayOf()

        val requestPermissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            hasReadPermission = permissions[Manifest.permission.READ_CALENDAR] == true
            hasWritePermission = permissions[Manifest.permission.WRITE_CALENDAR] == true
            hasFineLocation = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            hasCoarseLocation = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        }

        LaunchedEffect(Unit) {
            if (!Calendar.PermissionsRequest.hasReadCalendarPermission(context) || !Calendar.PermissionsRequest.hasWriteCalendarPermission(
                    context
                )
            ) {
                // Array of permissions to request
                permissionsArray = permissionsArray.plus(Manifest.permission.READ_CALENDAR)
                permissionsArray = permissionsArray.plus(Manifest.permission.WRITE_CALENDAR)
            } else {
                hasReadPermission = true
                hasWritePermission = true
            }
            if (!context.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) || !context.hasPermission(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ) {
                permissionsArray = permissionsArray.plus(Manifest.permission.ACCESS_FINE_LOCATION)
                permissionsArray = permissionsArray.plus(Manifest.permission.ACCESS_COARSE_LOCATION)
            } else {
                hasFineLocation = true
                hasCoarseLocation = true
            }
            if (permissionsArray.isNotEmpty()) {
                requestPermissionLauncher.launch(permissionsArray)
            }
        }

        if (hasReadPermission && hasWritePermission) {
            events = Calendar.fetchCalendarEvents(context)
            addNotifPush(events)
            //EventList(events, Modifier.padding(innerPadding))
        } else {
            Log.d(TAG, "PermissionsContent: Calendar permissions not granted")
        }
        if (hasFineLocation && hasCoarseLocation) {
            locationHandler.initLocation(this)
            //locationHandler.startLocationUpdates(this)
        } else {
            Log.d(TAG, "PermissionsContent: Location permissions not granted")
        }
    }

    private fun addNotifPush(events: List<Event>) {
        if (initNotif) return
        workManager.cancelAllWorkByTag("EventReminderWorker")
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
fun OsmdroidMapView() {
    val context = LocalContext.current
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            val mapView = MapView(context)
            mapView.setTileSource(TileSourceFactory.MAPNIK)
            mapView.setBuiltInZoomControls(true)
            mapView.setMultiTouchControls(true)
            val mapController = mapView.controller
            mapController.setZoom(15)
            val startPoint = GeoPoint(locationHandler.currentLocation!!.latitude,
                locationHandler.currentLocation!!.longitude)
            mapController.setCenter(startPoint);
            val compassOverlay = CompassOverlay(context, InternalCompassOrientationProvider(context), mapView)
            compassOverlay.enableCompass()
            mapView.overlays.add(compassOverlay)
            mapView
        }
    )
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
fun MyColumn(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onResult: () -> Unit
) {
    val ctx = LocalContext.current
    val app = application
    val tree = app.chatbotTree
    val messages = rememberMutableStateListOf(tree.getQuestion())
    val crtScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val animated = rememberMutableStateListOf<Boolean>()
    val user = app.currentUser

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
            val list: Collection<String>?
        ) {
            constructor() : this(false, "Choisissez une option ci-dessus", TypeAction.None, 0, null)
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

        fun addAnswer(id: Int, answer: String? = null) {
            reset()
            messages += answer ?: tree.getAnswerText(id)
            tree.selectAnswer(id, user)

            crtScope.launch {
                lazyListState.animateScrollToItem(messages.size)
                delay(1.seconds)
                messages += tree.getQuestion()
                answers = tree.getAnswersId().map { i -> tree.getAnswerText(i) }
                lazyListState.animateScrollToItem(messages.size)

                if (tree.getBotAction() == TypeAction.AfficherResultat) {
                    delay(5.seconds)
                    onResult()
                }
            }
        }

        ProposalList(proposals = answers) {
            answers = emptyList()
            Log.i(TAG, "Choose '$it'")
            val i = tree.getAnswersId().first { i -> tree.getAnswerText(i) == it }
            when (val act = tree.getUserAction(i)) {
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
                        app.activitiesRepository.getVillesDisponible()
                    )
                    return@ProposalList
                }

                TypeAction.AfficherResultat -> {
                    Log.i(
                        TAG,
                        "MyColumn: Affichage des résultats: ${
                            app.activitiesRepository.getResultats(
                                app
                            )
                        }"
                    )
                }

                TypeAction.Geolocalisation -> {
                    app.activitiesRepository.setLocation(currentLocation ?: Location(""))
                    addAnswer(
                        i,
                        "Je suis ici : ${locationHandler.currentLocation!!.longitude}, ${locationHandler.currentLocation?.latitude}"
                    )
                    return@ProposalList
                }

//                TypeAction.ChoisirPassions -> TODO()

                TypeAction.ActivitePhysique -> user.addType(SPORT)

                TypeAction.ActiviteCulturelle -> user.addType(CULTURE)

                TypeAction.ChoisirPassions -> {
                    val passions = ActivitiesRepository.passionList
                    enableSearchBar("Choisissez une passion", act, i, passions)
                    return@ProposalList
                }
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
                    addAnswer(sbState.answerId, "Je veux y aller le $it")
                    activitiesRepository.setDate(it)
                }

                TypeAction.EntrerDistance -> {
                    addAnswer(sbState.answerId, "Je veux une distance de $it km")
                    activitiesRepository.setDistance(it.toInt())
                }

                TypeAction.EntrerVille -> {
                    user.addVille(it)
                    val text = if ("AEIOUaeiou".indexOf(it.first()) != -1) "d'$it" else "de $it"
                    addAnswer(sbState.answerId, "Je souhaite faire mon activité dans les alentours de la ville $text")
                }

                else -> {}
            }
            Log.i(TAG, "Searched $it")
        }
    }
}
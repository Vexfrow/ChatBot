package fr.c1.chatbot

import android.Manifest
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.os.Looper
import androidx.preference.PreferenceManager
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import fr.c1.chatbot.ui.theme.ChatBotTheme
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.config.Configuration.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
class MainActivity : ComponentActivity() {
    private lateinit var myOpenMapView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: Location? = null
    // Provides location updates for while-in-use feature.

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var startButton: Button
    private lateinit var stopButton: Button

    private lateinit var outputTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //handle permissions first, before map is created. not depicted here

        //load/initialize the osmdroid configuration, this can be done
        // This won't work unless you have imported this: org.osmdroid.config.Configuration.*
        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        /*enableEdgeToEdge()
        setContent {
            ChatBotTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        val tree = (application as ChatBot).chatbotTree
                        val messages = rememberMutableStateListOf(tree.getQuestion())

                        val crtScope = rememberCoroutineScope()
                        val lazyListState = rememberLazyListState()

                        val animated = rememberMutableStateListOf<Boolean>()

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
                                    SpeechBubble(
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
                                        SpeechBubble(
                                            modifier = Modifier.align(Alignment.CenterEnd),
                                            text = message,
                                            color = MaterialTheme.colorSchemeExtension.user,
                                            reversed = true
                                        )
                                    }
                            }
                        }

                        var answers by rememberMutableStateOf(value = tree.getAnswersId()
                            .map { tree.getAnswerText(it) })

                        ProposalList(proposals = answers) {
                            answers = emptyList()
                            Log.i(TAG, "Choose '$it'")
                            val i = tree.getAnswersId()
                                .first { i -> tree.getAnswerText(i) == it }
                            messages += it
                            tree.selectAnswer(i)

                            crtScope.launch {
                                lazyListState.animateScrollToItem(messages.size)
                                delay(1.seconds)
                                messages += tree.getQuestion()
                                answers = tree.getAnswersId().map { i -> tree.getAnswerText(i) }
                                lazyListState.animateScrollToItem(messages.size)
                            }
                        }

                        var searchBarEnabled by rememberMutableStateOf(value = true)
                        var searchBarText by rememberMutableStateOf(value = "Search")

                        MySearchBar(
                            placeholder = searchBarText,
                            enabled = searchBarEnabled
                        ) { Log.i(TAG, "Searched $it") }
                    }
                }
            }
        }*/
        locationRequest = createLocationRequest()
        initLocalisation()

        setContentView(R.layout.activity_main)

        sharedPreferences =
            getSharedPreferences("preference_file_key", Context.MODE_PRIVATE)

        //foregroundOnlyLocationButton = findViewById(R.id.foreground_only_location_button)
        //outputTextView = findViewById(R.id.output_text_view)
        startButton = findViewById(R.id.startButton)
        startButton.setOnClickListener {
            // TODO: Step 1.0, Review Permissions: Checks and requests if needed.
            Log.d(TAG, "Started location updates")
            startLocationUpdates() ?: Log.d(TAG, "Service Not Bound")
            Log.d(TAG, ""+currentLocation)
        }
        stopButton = findViewById(R.id.stopButton)
        stopButton.setOnClickListener {
            Log.d(TAG, "Stopped location updates")
            stopLocationUpdates() ?: Log.d(TAG, "Service Not Bound")
            Log.d(TAG, ""+currentLocation)
        }

        //setContentView(R.layout.maplayout)
        myOpenMapView = findViewById<MapView>(R.id.map);
        myOpenMapView.setTileSource(TileSourceFactory.MAPNIK)
        myOpenMapView.setBuiltInZoomControls(true);
        myOpenMapView.setClickable(true);
        myOpenMapView.getController().setZoom(15);
    }
    private fun initLocalisation() {

        Log.i(ContentValues.TAG, "INIT LOCATION")
        //var hasLocationPermission by remember { mutableStateOf(false) }
        //var events by remember { mutableStateOf<List<Event>>(emptyList())) }
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            /*val requestPermissionLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()) {
                    permissions ->
                hasLocationPermission = (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) &&
                        (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true)
                if( hasLocationPermission){
                    //events = getLocationEvent(context)
                }
            }
            LaunchedEffect(Unit) {
                requestPermissionLauncher.launch(
                    arrayOf(
                        //Manifest.permission.RECEIVE_BOOT_COMPLETED,
                        //Manifest.permission.POST_NOTIFICATIONS
                    )
                )
            }*/
            return
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        //Get localization
        Log.d(ContentValues.TAG, "lOCATION REQUEST : ${locationRequest}")
        Log.d(ContentValues.TAG, "LOCATION CALLBACK")
        // TODO: Step 1.4, Initialize the LocationCallback.
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                if (locationResult.lastLocation != null) {

                    // Normally, you want to save a new location to a database. We are simplifying
                    // things a bit and just saving it as a local variable, as we only need it again
                    // if a Notification is created (when user navigates away from app).
                    currentLocation = locationResult.lastLocation
                    Log.d(TAG,"Latitude: "+currentLocation?.getLatitude() + ", Longitude: "+ currentLocation?.getLongitude())
                    val gp = GeoPoint(currentLocation?.getLatitude() ?: 0.00,
                        currentLocation?.getLongitude() ?: 0.00
                    )
                    myOpenMapView.getController().setCenter(gp);
                } else {
                    Log.d(TAG, "Location information isn't available.")
                }
            }
        }
        //last location
        /*fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
            }
        */
    }
    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            Log.i(ContentValues.TAG, "PERMISSIONS GRANTED")
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
    private fun createLocationRequest() : LocationRequest {
        return LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,10000)
            .setMinUpdateIntervalMillis(5000)
            .build()
    }
    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        ChatBotTheme {
            Greeting("Android")
        }
    }
}
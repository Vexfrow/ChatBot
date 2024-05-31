package fr.c1.chatbot

import android.Manifest
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.snackbar.Snackbar
import fr.c1.chatbot.composable.MySearchBar
import fr.c1.chatbot.composable.ProposalList
import fr.c1.chatbot.composable.SpeechBubble
import fr.c1.chatbot.ui.theme.ChatBotTheme
import fr.c1.chatbot.ui.theme.colorSchemeExtension
import fr.c1.chatbot.utils.LocationHandler
import fr.c1.chatbot.utils.rememberMutableStateListOf
import fr.c1.chatbot.utils.rememberMutableStateOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
private const val KEY_FOREGROUND_ENABLED = "tracking_foreground_location"
class MainActivity : ComponentActivity() {
    // Provides location updates for while-in-use feature.
    private var locationHandler: LocationHandler? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var startButton: Button
    private lateinit var stopButton: Button

    private lateinit var outputTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        setContentView(R.layout.activity_main)

        sharedPreferences =
            getSharedPreferences("preference_file_key", Context.MODE_PRIVATE)

        //foregroundOnlyLocationButton = findViewById(R.id.foreground_only_location_button)
        //outputTextView = findViewById(R.id.output_text_view)
        startButton = findViewById(R.id.startButton)
        //stopButton = findViewById(R.id.stopButton)
        startButton.setOnClickListener {
            val enabled = sharedPreferences.getBoolean(
                KEY_FOREGROUND_ENABLED, false)

            if (enabled) {
                locationHandler?.unsubscribeToLocationUpdates()
            } else {
                // TODO: Step 1.0, Review Permissions: Checks and requests if needed.
                if (foregroundPermissionApproved()) {
                    startForegroundService()
                    locationHandler?.subscribeToLocationUpdates()
                        ?: Log.d(TAG, "Service Not Bound")
                } else {
                    stopForegroundService()
                    requestForegroundPermissions()
                }
            }
        }
        /*myOpenMapView = (MapView)findViewById(R.id.mapview);
        myOpenMapView.setBuiltInZoomControls(true);
        myOpenMapView.setClickable(true);
        myOpenMapView.getController().setZoom(15);*/
    }
    private fun startForegroundService() {
        val serviceIntent = Intent(this, LocationHandler::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }

    private fun stopForegroundService() {
        val serviceIntent = Intent(this, LocationHandler::class.java)
        stopService(serviceIntent)
    }
    // TODO: Step 1.0, Review Permissions: Handles permission result.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionResult")

        when (requestCode) {
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE -> when {
                grantResults.isEmpty() ->
                    // If user interaction was interrupted, the permission request
                    // is cancelled and you receive empty arrays.
                    Log.d(TAG, "User interaction was cancelled.")
                grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                    // Permission was granted.
                    locationHandler?.subscribeToLocationUpdates()
                else -> {
                    // Permission denied.
                    Log.d(TAG, "PERMISSION DENIED")
                }
            }
        }
    }
    // TODO: Step 1.0, Review Permissions: Method checks if permissions approved.
    private fun foregroundPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
    // TODO: Step 1.0, Review Permissions: Method requests permissions.
    private fun requestForegroundPermissions() {
        val provideRationale = foregroundPermissionApproved()

        // If the user denied a previous request, but didn't check "Don't ask again", provide
        // additional rationale.
        if (provideRationale) {
            Snackbar.make(

                findViewById(0),
                "permit",
                Snackbar.LENGTH_LONG
            )
                .setAction("ok") {
                    // Request permission
                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
                    )
                }
                .show()
        } else {
            Log.d(TAG, "Request foreground only permission")
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
            )
        }
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
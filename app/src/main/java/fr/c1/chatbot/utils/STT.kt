package fr.c1.chatbot.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

private const val TAG = "STT"

class STT(ctx: Context) {
    private var speechRecognizer: SpeechRecognizer
    private val REQUEST_CODE_SPEECH_INPUT = 1

   init {
       speechRecognizer = SpeechRecognizer.createSpeechRecognizer(ctx).apply {
           setRecognitionListener(object : RecognitionListener {
               override fun onReadyForSpeech(params: Bundle?) {}
               override fun onBeginningOfSpeech() {}
               override fun onRmsChanged(rmsdB: Float) {}
               override fun onBufferReceived(buffer: ByteArray?) {}
               override fun onEndOfSpeech() {}
               override fun onError(error: Int) {
                   Log.e(TAG, "onError: $error")
               }

               override fun onResults(results: Bundle?) {
                   val matches =
                       results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                   if (!matches.isNullOrEmpty()) {
                       val recognizedText = matches[0]
                       // Handle the recognized text
                   }
               }

               override fun onPartialResults(partialResults: Bundle?) {}
               override fun onEvent(eventType: Int, params: Bundle?) {}
           })
       }
   }

    fun startSpeechRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fr-FR")
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Parlez pour convertir en texte")
        }
        speechRecognizer.startListening(intent)
    }


    @Composable
    fun permission() {
        val context = LocalContext.current

        val speechRecognizerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                startSpeechRecognition()
            } else {
                Toast.makeText(
                    context,
                    "Permission refusée",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


}

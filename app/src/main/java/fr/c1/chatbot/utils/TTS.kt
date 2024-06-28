package fr.c1.chatbot.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import android.util.Log
import java.util.Locale
import java.util.UUID

private const val TAG = "TTS"

/** TTS wrapper */
class TTS(ctx: Context) {
    private object UtteranceLst : UtteranceProgressListener() {
        override fun onStart(utteranceId: String?) {}

        override fun onDone(utteranceId: String?) {}

        @Deprecated("Deprecated in Java")
        override fun onError(utteranceId: String) {
            Log.e(TAG, "Error on speak by $utteranceId")
        }

        override fun onError(utteranceId: String?, errorCode: Int) {
            Log.e(TAG, "Error on speak by $utteranceId: $errorCode")
        }
    }

    /** @see [TextToSpeech] */
    private var _tts: TextToSpeech? = null

    /** @see [_tts] */
    private val tts: TextToSpeech get() = _tts!!

    /** Indicate if the system is inited */
    private var inited: Boolean = false

    /** Queue for speak when ![inited] */
    private var list: MutableList<Pair<CharSequence, Boolean>>? = null

    /** Get the list of the voices avaible */
    private val voices: List<Voice>
        get() = tts.voices
            .filter { v -> v.locale == Locale.FRANCE && !v.isNetworkConnectionRequired }

    /** Get the current selected voice */
    private var voice: Voice
        get() = tts.voice
        set(value) {
            tts.voice = value
        }

    init {
        _tts = TextToSpeech(ctx) {
            if (it == TextToSpeech.ERROR) {
                Log.e(TAG, "TTS is cannot be initialized!")
                return@TextToSpeech
            }

            when (tts.setLanguage(Locale.FRANCE)) {
                TextToSpeech.LANG_MISSING_DATA -> Log.e(TAG, "The french data is missing")
                TextToSpeech.LANG_NOT_SUPPORTED -> Log.e(TAG, "The french is not supported")
            }

            Log.i(TAG, "Voices avaible: $voices")
            Log.i(TAG, "Current voice: $voice")

            tts.setOnUtteranceProgressListener(UtteranceLst)
            inited = true

            if (list != null)
                for (l in list!!)
                    speak(l.first, l.second)

            list = null
        }
    }

    /** @see TextToSpeech.speak */
    fun speak(text: CharSequence, flush: Boolean = false) {
        if (!inited) {
            if (list == null)
                list = mutableListOf()

            list!!.add(text to flush)
            return
        }

        val id = UUID.randomUUID()

        Log.i(TAG, "speak: Saying $text on $id")

        if (tts.speak(
                text,
                if (flush) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD,
                null,
                id.toString()
            ) == TextToSpeech.ERROR
        ) {
            Log.e(TAG, "speak: $id cannot speak")
        }
    }

    /** @see TextToSpeech.shutdown */
    fun shutdown() {
        tts.shutdown()
    }
}
package fr.c1.chatbot.utils

import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import fr.c1.chatbot.ChatBot
import fr.c1.chatbot.model.messageManager.Event
import kotlinx.coroutines.CoroutineScope
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SnapshotMutationPolicy
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.net.toUri
import androidx.work.WorkManager
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty0

/** Get the [ChatBot] application on a [Composable] */
val application: ChatBot
    @Composable
    get() = (LocalContext.current as Activity).application as ChatBot

/** Get the [ChatBot] application on a [Activity] */
val Activity.app: ChatBot get() = application as ChatBot

/** [LaunchedEffect] with [Unit] as single key */
@Composable
fun UnitLaunchedEffect(block: suspend CoroutineScope.() -> Unit) = LaunchedEffect(Unit, block)

/**
 * [remember] a [MutableState] without any key
 *
 * @see mutableStateOf
 */
@Composable
fun <T> rememberMutableStateOf(
    value: T,
    policy: SnapshotMutationPolicy<T> = structuralEqualityPolicy()
): MutableState<T> = remember { mutableStateOf(value, policy) }

/**
 * [remember] a [MutableState] with a single key
 *
 * @see mutableStateOf
 */
@Composable
fun <T> rememberMutableStateOf(
    key1: Any?,
    value: T,
    policy: SnapshotMutationPolicy<T> = structuralEqualityPolicy()
): MutableState<T> = remember(key1) { mutableStateOf(value, policy) }

/** [remember] a [MutableState] with a single key */
@Composable
fun <T> rememberMutableStateListOf(): SnapshotStateList<T> = remember { mutableStateListOf() }

/**
 * [remember] a [SnapshotStateList] without any key
 *
 * @see mutableStateListOf
 */
@Composable
fun <T> rememberMutableStateListOf(vararg elements: T): SnapshotStateList<T> =
    remember { mutableStateListOf(*elements) }

/** Check if the user have [PackageManager.PERMISSION_GRANTED] a specified [permission] */
fun Context.hasPermission(permission: String) = ContextCompat.checkSelfPermission(
    this,
    permission
) == PackageManager.PERMISSION_GRANTED

operator fun TextUnit.plus(f: Float) = (this.value + f).sp
operator fun TextUnit.minus(f: Float) = (this.value - f).sp

/**
 * Set the value of a [KMutableProperty0] from the [SharedPreferences]
 *
 * @param V Type of the property
 * @param T Type of the object stored in the [SharedPreferences]
 * @param ref Reference of the object to set
 * @param ifNotNull Callback when the [ref] name exists:
 * (key: [String], defaultValue: [T]) -> settingsValue: [V]
 * @param defaultValue Default value to send to [ifNotNull]
 *
 * @return if the [SharedPreferences] doesn't contains the [KMutableProperty0.name], null
 * otherwise the result of [ifNotNull]
 */
fun <V, T> SharedPreferences.getNullable(
    ref: KMutableProperty0<V?>,
    ifNotNull: (String, T) -> V,
    defaultValue: T
) = ref.set(if (contains(ref.name)) ifNotNull(ref.name, defaultValue) else null)

/**
 * If the property is null, remove it from settings, otherwise store it
 *
 * Currently work only with [Uri]
 *
 * @param T Type of the property
 * @param ref Reference to the object to store
 */
fun <T> SharedPreferences.Editor.putOrRemove(ref: KProperty0<T>) = when (val value = ref.get()) {
    null -> remove(ref.name)
    is Uri -> putUri(ref.name, value)

    else -> throw NotImplementedError("We cannot save an object of ${ref.javaClass}")
}

/**
 * Copy the image at a specified [Uri] into the internal storage and store the new [Uri] into the
 * preferences
 *
 * @param ref Reference to the [Uri] of the image
 * @param context Android context
 */
fun SharedPreferences.Editor.saveImage(ref: KProperty0<Uri?>, context: Context) {
    val file = File(context.filesDir, ref.name)

    when (val value = ref.get()) {
        null -> {
            if (file.exists())
                file.delete()
            remove(ref.name)
        }

        else -> {
            if (value.lastPathSegment.equals("userImage") || value.lastPathSegment.equals("botImage"))
                return

            val inputStream = context.contentResolver.openInputStream(value)
            inputStream?.use { inp ->
                val out = FileOutputStream(file)
                val buffer = ByteArray(1024)
                var bytesRead: Int

                while (inp.read(buffer).also { bytesRead = it } != -1)
                    out.write(buffer, 0, bytesRead)

                out.close()
            }
            putUri(ref.name, file.toUri())
        }
    }
}

/** Get sp */
fun SharedPreferences.getSp(
    ref: KMutableProperty0<TextUnit>,
    defaultValue: TextUnit
) = ref.set(getFloat(ref.name, defaultValue.value).sp)

/** Put sp */
fun SharedPreferences.Editor.putSp(ref: KMutableProperty0<TextUnit>) =
    putFloat(ref.name, ref.get().value)

/** Get bool */
fun SharedPreferences.getBool(
    ref: KMutableProperty0<Boolean>,
    defaultValue: Boolean
) = ref.set(getBoolean(ref.name, defaultValue))

/** Put string */
fun SharedPreferences.Editor.putString(ref: KMutableProperty0<String>) =
    putString(ref.name, ref.get())

/** Get string */
fun SharedPreferences.getString(
    ref: KMutableProperty0<String>,
    defaultValue: String
) = getString(ref.name, defaultValue)?.let { ref.set(it) }

/** Put bool */
fun SharedPreferences.Editor.putBool(ref: KMutableProperty0<Boolean>) =
    putBoolean(ref.name, ref.get())

/** Get uri */
fun SharedPreferences.getUri(
    key: String,
    defaultValue: String
) = Uri.parse(getString(key, defaultValue))

/** Get uri */
fun SharedPreferences.getUri(
    ref: KMutableProperty0<Uri>,
    defaultValue: String
) = ref.set(getUri(ref.name, defaultValue))

/** Put uri */
fun SharedPreferences.Editor.putUri(
    key: String,
    value: Uri
) = putString(key, value.toString())

/**
 * Parse an [InputStream] into a list of [T]. Note that the first line corrspond to the header and
 * is ignored
 *
 * @param T Type of each csv line
 * @param csvIS [InputStream] of the csv
 * @param onLine Callback to map each csv line [T]
 */
fun <T> parseCsv(csvIS: InputStream, onLine: (Array<String>) -> T): List<T> {
    val csvParser = CSVParserBuilder()
        .withSeparator(';')
        .withIgnoreQuotations(false)
        .withEscapeChar('\\')
        .build()

    val csvReader = CSVReaderBuilder(InputStreamReader(csvIS))
        .withCSVParser(csvParser)
        .build()

    return csvReader
        .asSequence()
        .drop(1)
        .map(onLine)
        .toList()
}

/** Get color */
fun SharedPreferences.getColor(
    key: String,
    default: Color
) = Color(getInt(key, default.toArgb()))

/** Geet color */
fun SharedPreferences.getColor(
    ref: KMutableProperty0<Color>,
    defaultValue: Color
) = ref.set(getColor(ref.name, defaultValue))

/** Put color */
fun SharedPreferences.Editor.putColor(
    key: String,
    value: Color
) = putInt(key, value.toArgb())

/** Put color */
fun SharedPreferences.Editor.putColor(
    ref: KProperty0<Color>
): SharedPreferences.Editor = putColor(ref.name, ref.get())

/** Disable notification */
fun disableNotification(context: Context) {
    val workManager = WorkManager.getInstance(context)
    workManager.cancelAllWorkByTag("EventReminderWorker")
}

/** Enable notification */
fun enableNotification(context: Context) {
    val events = Calendar.fetchCalendarEvents(context)
    Event.Notifs.addNotification(events, context as ComponentActivity)
}

/** Get the foreground on this background color */
val Color.foreground: Color
    get() =
        if (ColorUtils.calculateLuminance(toArgb()) > .5) Color.Black
        else Color.White
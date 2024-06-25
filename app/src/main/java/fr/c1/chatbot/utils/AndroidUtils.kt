package fr.c1.chatbot.utils

import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import fr.c1.chatbot.ChatBot
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
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.DefaultFillType
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path
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
import fr.c1.chatbot.model.messageManager.Event
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty0

val application: ChatBot
    @Composable
    get() = (LocalContext.current as Activity).application as ChatBot

val Activity.app: ChatBot get() = application as ChatBot

/**
 * Unit launched effect
 *
 * @param block
 * @receiver
 */
@Composable
fun UnitLaunchedEffect(block: suspend CoroutineScope.() -> Unit) = LaunchedEffect(Unit, block)

/**
 * Remember mutable state of
 *
 * @param T
 * @param value
 * @param policy
 * @return
 */
@Composable
fun <T> rememberMutableStateOf(
    value: T,
    policy: SnapshotMutationPolicy<T> = structuralEqualityPolicy()
): MutableState<T> = remember { mutableStateOf(value, policy) }

/**
 * Remember mutable state of
 *
 * @param T
 * @param key1
 * @param value
 * @param policy
 * @return
 */
@Composable
fun <T> rememberMutableStateOf(
    key1: Any?,
    value: T,
    policy: SnapshotMutationPolicy<T> = structuralEqualityPolicy()
): MutableState<T> = remember(key1) { mutableStateOf(value, policy) }

/**
 * Remember mutable state list of
 *
 * @param T
 * @return
 */
@Composable
fun <T> rememberMutableStateListOf(): SnapshotStateList<T> = remember { mutableStateListOf() }

/**
 * Remember mutable state list of
 *
 * @param T
 * @param elements
 * @return
 */
@Composable
fun <T> rememberMutableStateListOf(vararg elements: T): SnapshotStateList<T> =
    remember { mutableStateListOf(*elements) }

/**
 * Has permission
 *
 * @param permission
 */
fun Context.hasPermission(permission: String) = ContextCompat.checkSelfPermission(
    this,
    permission
) == PackageManager.PERMISSION_GRANTED

/**
 * Plus
 *
 * @param f
 */
operator fun TextUnit.plus(f: Float) = (this.value + f).sp

/**
 * Minus
 *
 * @param f
 */
operator fun TextUnit.minus(f: Float) = (this.value - f).sp

/**
 * Material path c
 *
 * @param fill
 * @param fillAlpha
 * @param stroke
 * @param strokeAlpha
 * @param pathFillType
 * @param pathBuilder
 * @receiver
 */
inline fun ImageVector.Builder.materialPathC(
    fill: Color = Color.Black,
    fillAlpha: Float = 1f,
    stroke: Color? = null,
    strokeAlpha: Float = 1f,
    pathFillType: PathFillType = DefaultFillType,
    pathBuilder: PathBuilder.() -> Unit
) = path(
    fill = SolidColor(fill),
    fillAlpha = fillAlpha,
    stroke = if (stroke == null) null else SolidColor(stroke),
    strokeAlpha = strokeAlpha,
    strokeLineWidth = 1f,
    strokeLineCap = StrokeCap.Butt,
    strokeLineJoin = StrokeJoin.Bevel,
    strokeLineMiter = 1f,
    pathFillType = pathFillType,
    pathBuilder = pathBuilder
)

/**
 * Get nullable
 *
 * @param V
 * @param T
 * @param ref
 * @param ifNotNull
 * @param defaultValue
 * @receiver
 */
fun <V, T> SharedPreferences.getNullable(
    ref: KMutableProperty0<V?>,
    ifNotNull: (String, T) -> V,
    defaultValue: T
) = ref.set(if (contains(ref.name)) ifNotNull(ref.name, defaultValue) else null)

/**
 * Put or remove
 *
 * @param T
 * @param ref
 */
fun <T> SharedPreferences.Editor.putOrRemove(ref: KProperty0<T>) = when (val value = ref.get()) {
    null -> remove(ref.name)
    is Uri -> putUri(ref.name, value)

    else -> throw NotImplementedError("We cannot save an object of ${ref.javaClass}")
}

/**
 * Save image
 *
 * @param ref
 * @param context
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

/**
 * Get sp
 *
 * @param ref
 * @param defaultValue
 */
fun SharedPreferences.getSp(
    ref: KMutableProperty0<TextUnit>,
    defaultValue: TextUnit
) = ref.set(getFloat(ref.name, defaultValue.value).sp)

/**
 * Put sp
 *
 * @param ref
 */
fun SharedPreferences.Editor.putSp(ref: KMutableProperty0<TextUnit>) =
    putFloat(ref.name, ref.get().value)

/**
 * Get bool
 *
 * @param ref
 * @param defaultValue
 */
fun SharedPreferences.getBool(
    ref: KMutableProperty0<Boolean>,
    defaultValue: Boolean
) = ref.set(getBoolean(ref.name, defaultValue))

/**
 * Put string
 *
 * @param ref
 */
fun SharedPreferences.Editor.putString(ref: KMutableProperty0<String>) =
    putString(ref.name, ref.get())

/**
 * Get string
 *
 * @param ref
 * @param defaultValue
 */
fun SharedPreferences.getString(
    ref: KMutableProperty0<String>,
    defaultValue: String
) = getString(ref.name, defaultValue)?.let { ref.set(it) }

/**
 * Put bool
 *
 * @param ref
 */
fun SharedPreferences.Editor.putBool(ref: KMutableProperty0<Boolean>) =
    putBoolean(ref.name, ref.get())

/**
 * Get uri
 *
 * @param key
 * @param defaultValue
 */
fun SharedPreferences.getUri(
    key: String,
    defaultValue: String
) = Uri.parse(getString(key, defaultValue))

/**
 * Get uri
 *
 * @param ref
 * @param defaultValue
 */
fun SharedPreferences.getUri(
    ref: KMutableProperty0<Uri>,
    defaultValue: String
) = ref.set(getUri(ref.name, defaultValue))

/**
 * Put uri
 *
 * @param key
 * @param value
 */
fun SharedPreferences.Editor.putUri(
    key: String,
    value: Uri
) = putString(key, value.toString())

/**
 * Parse csv
 *
 * @param csvIS
 * @param onLine
 * @receiver
 * @return
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

/**
 * Put color
 *
 * @param ref
 * @return
 */
fun SharedPreferences.Editor.putColor(
    ref: KMutableProperty0<Color>
): SharedPreferences.Editor = putInt(ref.name, ref.get().value.toInt())

/**
 * Get color
 *
 * @param ref
 * @param defaultValue
 */
fun SharedPreferences.getColor(
    ref: KMutableProperty0<Color>,
    defaultValue: Int
) = getInt(ref.name, defaultValue)

/**
 * Disable notification
 *
 * @param context
 */
fun disableNotification(context: Context) {
    val workManager = WorkManager.getInstance(context)
    workManager.cancelAllWorkByTag("EventReminderWorker")
}

/**
 * Enable notification
 *
 * @param context
 */
fun enableNotification(context: Context) {
    val events = Calendar.fetchCalendarEvents(context)
    Event.Notifs.addNotification(events, context as ComponentActivity)
}

val Color.foreground: Color
    get() =
        if (ColorUtils.calculateLuminance(toArgb()) > .5) Color.Black
        else Color.White
package fr.c1.chatbot.utils

import fr.c1.chatbot.ChatBot
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.vector.DefaultFillType
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty0

val application: ChatBot
    @Composable
    get() = (LocalContext.current as ComponentActivity).application as ChatBot

@Composable
fun <T> rememberMutableStateOf(
    value: T,
    policy: SnapshotMutationPolicy<T> = structuralEqualityPolicy()
): MutableState<T> = remember { mutableStateOf(value, policy) }

@Composable
fun <T> rememberMutableStateListOf(): SnapshotStateList<T> = remember { mutableStateListOf() }

@Composable
fun <T> rememberMutableStateListOf(vararg elements: T): SnapshotStateList<T> =
    remember { mutableStateListOf(*elements) }

fun Context.hasPermission(permission: String) = ContextCompat.checkSelfPermission(
    this,
    permission
) == PackageManager.PERMISSION_GRANTED

operator fun TextUnit.plus(f: Float) = (this.value + f).sp
operator fun TextUnit.minus(f: Float) = (this.value - f).sp

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

fun <V, T> SharedPreferences.getNullable(
    ref: KMutableProperty0<V?>,
    ifNotNull: (String, T) -> V,
    defaultValue: T
) = ref.set(if (contains(ref.name)) ifNotNull(ref.name, defaultValue) else null)

fun <T> SharedPreferences.Editor.putOrRemove(ref: KProperty0<T>) = when (val value = ref.get()) {
    null -> remove(ref.name)
    is Uri -> putUri(ref.name, value)

    else -> throw NotImplementedError("We cannot save an object of ${ref.javaClass}")
}

fun SharedPreferences.getSp(
    ref: KMutableProperty0<TextUnit>,
    defaultValue: TextUnit
) = ref.set(getFloat(ref.name, defaultValue.value).sp)

fun SharedPreferences.Editor.putSp(ref: KMutableProperty0<TextUnit>) =
    putFloat(ref.name, ref.get().value)

fun SharedPreferences.getBool(
    ref: KMutableProperty0<Boolean>,
    defaultValue: Boolean
) = ref.set(getBoolean(ref.name, defaultValue))

fun SharedPreferences.Editor.putBool(ref: KMutableProperty0<Boolean>) =
    putBoolean(ref.name, ref.get())

fun SharedPreferences.getUri(
    key: String,
    defaultValue: String
) = Uri.parse(getString(key, defaultValue))

fun SharedPreferences.getUri(
    ref: KMutableProperty0<Uri>,
    defaultValue: String
) = ref.set(getUri(ref.name, defaultValue))

fun SharedPreferences.Editor.putUri(
    key: String,
    value: Uri
) = putString(key, value.toString())
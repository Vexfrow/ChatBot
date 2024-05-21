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
import androidx.compose.ui.platform.LocalContext

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
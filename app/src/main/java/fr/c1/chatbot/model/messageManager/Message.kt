package fr.c1.chatbot.model.messageManager

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class Message(
    var messageContent: String = "",
    var isUser: Boolean = false,
    var isScript : Boolean = false, //True if it's a bot message and is in the script
    showed : Boolean = false
) {
    var showed: Boolean by mutableStateOf(showed)
}